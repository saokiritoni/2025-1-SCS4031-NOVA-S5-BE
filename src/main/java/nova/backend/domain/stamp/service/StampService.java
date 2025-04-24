package nova.backend.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.dto.response.StampHistoryResponseDTO;
import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    // TODO: 로그 메시지 삭제

    public void accumulateStamp(User staff, String targetQrCode, Long cafeId, int count) {
        log.info("[스탬프 적립 시작] staff={}, targetQrCode={}, cafeId={}, count={}",
                staff.getEmail(), targetQrCode, cafeId, count);

        // 1. 권한 체크
        if (!(staff.getRole() == Role.OWNER || staff.getRole() == Role.STAFF)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 2. QR 코드로 사용자 조회
        User targetUser = userRepository.findByQrCodeValue(targetQrCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 3. 기존 로직은 targetUser의 userId 기준으로 동작
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        int stampsToAdd = count;

        while (stampsToAdd > 0) {
            StampBook currentBook = stampBookRepository
                    .findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(targetUser.getUserId(), cafeId)
                    .orElseGet(() -> stampBookRepository.save(
                            StampBook.builder()
                                    .user(targetUser)
                                    .cafe(cafe)
                                    .isCompleted(false)
                                    .rewardClaimed(false)
                                    .inHome(false)
                                    .build()
                    ));

            int currentCount = stampRepository.countByStampBook_StampBookId(currentBook.getStampBookId());
            int max = cafe.getMaxStampCount();
            int availableSpace = max - currentCount;

            int toSaveNow = Math.min(availableSpace, stampsToAdd);

            if (toSaveNow <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST);
            }

            for (int i = 0; i < toSaveNow; i++) {
                stampRepository.save(Stamp.builder().stampBook(currentBook).build());
            }

            if (currentCount + toSaveNow == max) {
                currentBook.markAsCompleted();
            }

            stampsToAdd -= toSaveNow;
        }

        log.info("[스탬프 적립 종료] staff={}, targetUser={}, cafeId={}, count={}", staff.getEmail(), targetUser.getEmail(), cafeId, count);
    }


    public List<StampHistoryResponseDTO> getStampHistory(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);

        return stampBooks.stream().map(stampBook -> {
            List<LocalDateTime> stampDates = stampRepository.findByStampBook_StampBookId(stampBook.getStampBookId())
                    .stream()
                    .map(Stamp::getCreatedAt)
                    .toList();

            return StampHistoryResponseDTO.builder()
                    .stampBookId(stampBook.getStampBookId())
                    .cafeName(stampBook.getCafe().getCafeName())
                    .stampDates(stampDates)
                    .stampCount(stampDates.size())
                    .maxStampCount(stampBook.getCafe().getMaxStampCount())
                    .isCompleted(stampBook.isCompleted())
                    .completedAt(stampBook.isCompleted() ? stampBook.getUpdatedAt() : null)
                    .rewardClaimed(stampBook.isRewardClaimed())
                    .rewardClaimedAt(stampBook.isRewardClaimed() ? stampBook.getUpdatedAt() : null)
                    .build();
        }).toList();
    }

}
