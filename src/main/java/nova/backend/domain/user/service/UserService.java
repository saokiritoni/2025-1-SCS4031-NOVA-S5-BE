package nova.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.request.UserLoginRequestDTO;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.SocialType;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import nova.backend.global.util.QrCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private User getExistedUser(String socialId, SocialType socialType) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);
    }

    // 사용자 정보 저장
    @Transactional
    public User saveUser(String socialId, String imageUrl, String kakaoName, UserLoginRequestDTO userLoginRequest) {
        User existedUser = getExistedUser(socialId, userLoginRequest.socialType());

        if (existedUser == null) {
            String qrCode = QrCodeGenerator.generate();
            System.out.println("🔍 Generated QR Code = " + qrCode); // TODO: 삭제하기

            User newUser = User.builder()
                    .socialId(socialId)
                    .socialType(userLoginRequest.socialType())
                    .profileImageUrl(imageUrl)
                    .role(Role.USER)
                    .name(kakaoName)
                    .qrCodeValue(qrCode)
                    .build();

            User savedUser = userRepository.save(newUser);
            System.out.println("✅ User saved: " + savedUser.getUserId()); // TODO: 삭제하기

            return savedUser;

        }

        if (existedUser.getQrCodeValue() == null) {
            existedUser.updateQrCode(QrCodeGenerator.generate());
        }

        return existedUser;
    }

    // 내 profile QR 조회
    public QrCodeResponseDTO getMyQrCode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return QrCodeResponseDTO.from(user.getQrCodeValue());
    }

}
