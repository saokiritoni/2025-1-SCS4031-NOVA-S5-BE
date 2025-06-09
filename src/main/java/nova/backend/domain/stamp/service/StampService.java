package nova.backend.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.dto.response.RecentStampResponseDTO;
import nova.backend.domain.stamp.dto.response.StaffStampViewResponseDTO;
import nova.backend.domain.stamp.dto.response.StampHistoryResponseDTO;
import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.stampBook.service.UserStampBookService;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final UserStampBookService userStampBookService;

    public void accumulateStamp(CustomUserDetails userDetails, String targetQrCode, int count) {
        log.info("[스탬프 적립 시작] staff={}, qrCodeValue={}, selectedCafeId={}, count={}",
                userDetails.getEmail(), targetQrCode, userDetails.getSelectedCafeId(), count);

        // 권한 체크
        if (!(userDetails.getRole() == Role.OWNER || userDetails.getRole() == Role.STAFF)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 선택된 카페 확인
        Long selectedCafeId = userDetails.getSelectedCafeId();
        if (selectedCafeId == null) {
            throw new BusinessException(ErrorCode.CAFE_NOT_SELECTED);
        }

        Cafe cafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 대상 사용자 조회
        User targetUser = userRepository.findByQrCodeValue(targetQrCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 스탬프 적립
        int stampsToAdd = count;
        while (stampsToAdd > 0) {
            StampBook currentBook = userStampBookService.getOrCreateValidStampBook(targetUser, cafe);

            int currentCount = stampRepository.countByStampBook_StampBookId(currentBook.getStampBookId());
            int max = cafe.getMaxStampCount();
            int space = max - currentCount;

            int toAddNow = Math.min(space, stampsToAdd);
            if (toAddNow <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST);
            }
            for (int i = 0; i < toAddNow; i++) {
                stampRepository.save(Stamp.builder().stampBook(currentBook).build());
            }
            if (currentCount + toAddNow == max) {
                currentBook.markAsCompleted();
            }
            stampsToAdd -= toAddNow;
        }

        log.info("[스탬프 적립 종료] staff={}, targetUser={}, cafeId={}, count={}",
                userDetails.getEmail(), targetUser.getEmail(), selectedCafeId, count);
    }


    public List<StampHistoryResponseDTO> getStampHistory(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);

        return stampBooks.stream()
                .map(stampBook -> {
                    List<Stamp> stamps = stampRepository.findByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampHistoryResponseDTO.fromEntity(stampBook, stamps);
                })
                .toList();
    }
    
    @Transactional(readOnly = true)
    public StaffStampViewResponseDTO getStampHistoryForStaffView(String qrCodeValue, CustomUserDetails userDetails) {
        // 권한 체크
        if (!(userDetails.getRole() == Role.OWNER || userDetails.getRole() == Role.STAFF)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Long selectedCafeId = userDetails.getSelectedCafeId();
        if (selectedCafeId == null) {
            throw new BusinessException(ErrorCode.CAFE_NOT_SELECTED);
        }

        Cafe staffCafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        User targetUser = userRepository.findByQrCodeValue(qrCodeValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(targetUser.getUserId());
        List<StampHistoryResponseDTO> history = stampBooks.stream()
                .map(sb -> {
                    List<Stamp> stamps = stampRepository.findByStampBook_StampBookId(sb.getStampBookId());
                    return StampHistoryResponseDTO.fromEntity(sb, stamps);
                })
                .toList();

        List<Stamp> recentStamps = stampRepository.findTop3ByStampBook_Cafe_CafeIdOrderByCreatedAtDesc(selectedCafeId);
        List<RecentStampResponseDTO> recentStampDtos = recentStamps.stream()
                .map(RecentStampResponseDTO::fromEntity)
                .toList();

        int rewardCount = stampBookRepository
                .countByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalse(
                        targetUser.getUserId(), selectedCafeId);


        return StaffStampViewResponseDTO.from(targetUser, staffCafe, rewardCount, history, recentStampDtos);
    }

}
