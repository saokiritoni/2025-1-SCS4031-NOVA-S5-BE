package nova.backend.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.request.UserTokenRequestDTO;
import nova.backend.domain.user.dto.response.UserTokenResponseDTO;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.auth.jwt.JwtProvider;
import nova.backend.global.error.exception.EntityNotFoundException;
import nova.backend.global.error.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static nova.backend.global.error.ErrorCode.JSON_PARSING_FAILED;
import static nova.backend.global.error.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public String issueNewAccessToken(Long userId) {
        Role role = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND))
                .getRole();

        return jwtProvider.getIssueToken(userId, role, true);
    }

    public String issueNewRefreshToken(Long userId) {
        Role role = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND))
                .getRole();

        return jwtProvider.getIssueToken(userId, role, false);
    }

    public UserTokenResponseDTO issueTempToken(Long userId) {
        String accessToken = issueNewAccessToken(userId);
        String refreshToken = issueNewRefreshToken(userId);
        return UserTokenResponseDTO.of(accessToken, refreshToken);
    }

    public UserTokenResponseDTO issueToken(Long userId, Role role) {

        String accessToken = jwtProvider.getIssueToken(userId, role, true);

        // refresh token이 이미 존재하면 기존 토큰 사용하고, 없으면 새로 발급
        String redisKey = "RT:" + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

        if (storedRefreshToken == null) {
            storedRefreshToken = issueNewRefreshToken(userId);

            redisTemplate.opsForValue().set(redisKey, storedRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        return UserTokenResponseDTO.of(accessToken, storedRefreshToken);
    }

    public UserTokenResponseDTO reissue(UserTokenRequestDTO userTokenRequest){
        Long userId;
        try {
            userId = Long.valueOf(jwtProvider.decodeJwtPayloadSubject(userTokenRequest.accessToken()));
        } catch (JsonProcessingException e) {
            throw new UnauthorizedException(JSON_PARSING_FAILED);
        }

        String refreshToken = userTokenRequest.refreshToken();
        String redisKey = "RT:" + userId;

        // 리프레시 토큰 검증 (리프레시 토큰 만료시 재로그인 필요)
        jwtProvider.validateRefreshToken(refreshToken);

        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

        // 요청된 리프레시 토큰과 저장된 redis 저장된 리프레시 토큰 비교 검증
        jwtProvider.equalsRefreshToken(refreshToken, storedRefreshToken);

        String newAccessToken = issueNewAccessToken(userId);
        String newRefreshToken = issueNewRefreshToken(userId);

        redisTemplate.opsForValue().set(redisKey, newRefreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);

        return UserTokenResponseDTO.of(newAccessToken, newRefreshToken);
    }

    public void logout(Long userId) {
        String redisKey = "RT:" + userId;
        redisTemplate.delete(redisKey);
    }
}
