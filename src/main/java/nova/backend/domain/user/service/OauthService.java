package nova.backend.domain.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.request.UserLoginRequestDTO;
import nova.backend.domain.user.dto.response.UserTokenResponseDTO;
import nova.backend.domain.user.entity.SocialType;
import nova.backend.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final OauthTokenProvider oauthTokenProvider;
    private final OauthUserResourceProvider oauthUserResourceProvider;
    private final TokenService tokenService;
    private final UserService userService;

    @Transactional
    public UserTokenResponseDTO socialLogin(UserLoginRequestDTO userLoginRequest) {
        SocialType socialType = userLoginRequest.socialType();

        // 1. OAuth 토큰 가져오기
        String oauthToken = oauthTokenProvider.getOauthToken(userLoginRequest.code(), socialType);

        // 2. 사용자 정보 가져오기
        JsonNode userResource = oauthUserResourceProvider.getUserResource(oauthToken, socialType);
        Map<String, String> userInfo = oauthUserResourceProvider.extractUserInfo(userResource, socialType);
        String socialId = userInfo.get("socialId");
        String imageUrl = userInfo.get("imageUrl");
        String name = userInfo.get("name");

        // 3. 사용자 저장
        User user = userService.saveUser(socialId, imageUrl, name, userLoginRequest);

        // 4. JWT 토큰 발급
        return tokenService.issueToken(user.getUserId(), user.getRole());
    }





}
