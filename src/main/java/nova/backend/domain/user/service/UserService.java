package nova.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.request.UserLoginRequestDTO;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.SocialType;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 사용자 정보 저장
    @Transactional
    public User saveUser(String socialId, String imageUrl, String kakaoName, UserLoginRequestDTO userLoginRequest) {
        User existedUser = getExistedUser(socialId, userLoginRequest.socialType());

        if (existedUser == null) {
            User newUser = User.builder()
                    .socialId(socialId)
                    .socialType(userLoginRequest.socialType())
                    .profileImageUrl(imageUrl)
                    .role(Role.USER) // 기본 role
                    .name(kakaoName) // 카카오에서 받아온 이름 사용, 이후에 랜덤 생성으로 변경
                    .build();
            return userRepository.save(newUser);
        }

        return existedUser;
    }



    private User getExistedUser(String socialId, SocialType socialType) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);
    }
}
