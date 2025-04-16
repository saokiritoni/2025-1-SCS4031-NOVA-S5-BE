package nova.backend.global.auth;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.UnauthorizedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Spring Security에서 사용자를 로드하는 메소드
     * JWT의 subject를 userId로 간주하고, 이를 기반으로 유저 정보를 조회
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Long id;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
