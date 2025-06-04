package nova.backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.dto.request.UserLoginRequestDTO;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.domain.user.dto.response.UserStatusResponseDTO;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.SocialType;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import nova.backend.global.util.NicknameGenerator;
import nova.backend.global.util.QrCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;
    private final StampRepository stampRepository;
    private final StampBookRepository stampBookRepository;
    private User getExistedUser(String socialId, SocialType socialType) {
        return userRepository.findBySocialTypeAndSocialId(socialType, socialId).orElse(null);
    }

    // 사용자 정보 저장
    @Transactional
    public User saveUser(String socialId, String imageUrl, String kakaoName, UserLoginRequestDTO userLoginRequest) {
        User existedUser = getExistedUser(socialId, userLoginRequest.socialType());

        if (existedUser == null) {
            String qrCode = QrCodeGenerator.generate();
            String nickname = nicknameGenerator.generateNickname();

            User newUser = User.builder()
                    .socialId(socialId)
                    .socialType(userLoginRequest.socialType())
                    .profileImageUrl(imageUrl)
                    .role(userLoginRequest.role())
                    .name(nickname)
                    .qrCodeValue(qrCode)
                    .email(null)
                    .build();

            return userRepository.save(newUser);
        }

        if (existedUser.getQrCodeValue() == null) {
            existedUser.updateQrCode(QrCodeGenerator.generate());
        }

        /**
         * 권한 승급
         * USER로 회원가입 후, OWNER 서비스 사용 위해 로그인하면 OWNER로 권한 승급
         */
        if (existedUser.getRole() == Role.USER && userLoginRequest.role() == Role.OWNER) {
            existedUser.updateRole(Role.OWNER);
        }

        if (existedUser.getQrCodeValue() == null) {
            existedUser.updateQrCode(QrCodeGenerator.generate());
        }

        return existedUser;
    }

    // 내 profile QR 조회
    @Transactional(readOnly = true)
    public QrCodeResponseDTO getMyQrCode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return QrCodeResponseDTO.from(user.getQrCodeValue(), user.getName());
    }

    // 메인 페이지 유저 상태 표시 (QR 진입 전)
    @Transactional(readOnly = true)
    public UserStatusResponseDTO getUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        int todayStampCount = stampRepository.countByStampBook_User_UserIdAndCreatedAtBetween(
                userId, LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());

        int unusedRewardCount = stampBookRepository.countByUser_UserIdAndRewardClaimedTrueAndUsedFalse(userId);

        return UserStatusResponseDTO.of(user.getName(), todayStampCount, unusedRewardCount);
    }



}
