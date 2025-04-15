package nova.backend.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void accumulateStamp(Long userId, Long cafeId, int count) {
        log.info("[스탬프 적립 시작] userId={}, cafeId={}, count={}", userId, cafeId, count);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("사용자(userId={})를 찾을 수 없습니다.", userId);
                    return new BusinessException(ErrorCode.USER_NOT_FOUND);
                });

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> {
                    log.error("카페(cafeId={})를 찾을 수 없습니다.", cafeId);
                    return new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
                });

        int stampsToAdd = count;

        while (stampsToAdd > 0) {
            log.info("현재 남은 스탬프 적립 수량: {}", stampsToAdd);

            StampBook currentBook = stampBookRepository
                    .findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(userId, cafeId)
                    .orElseGet(() -> {
                        log.info("미완료 스탬프북이 없어서 새로 생성합니다.");
                        StampBook newBook = StampBook.builder()
                                .user(user)
                                .cafe(cafe)
                                .isCompleted(false)
                                .rewardClaimed(false)
                                .inHome(false)
                                .build();
                        StampBook saved = stampBookRepository.save(newBook);
                        log.info("새 스탬프북 생성 완료: stampBookId={}", saved.getStampBookId());
                        return saved;
                    });

            int currentCount = stampRepository.countByStampBook_StampBookId(currentBook.getStampBookId());
            int max = cafe.getMaxStampCount();
            int availableSpace = max - currentCount;

            log.info("현재 스탬프북 ID: {}, 적립됨: {}, 최대: {}, 남은 공간: {}",
                    currentBook.getStampBookId(), currentCount, max, availableSpace);

            int toSaveNow = Math.min(availableSpace, stampsToAdd);

            if (toSaveNow <= 0) {
                log.warn("더 이상 적립할 수 있는 공간이 없습니다. stampBookId={}", currentBook.getStampBookId());
                throw new BusinessException(ErrorCode.BAD_REQUEST);
            }

            for (int i = 0; i < toSaveNow; i++) {
                Stamp stamp = Stamp.builder()
                        .stampBook(currentBook)
                        .build();
                stampRepository.save(stamp);
            }

            log.info("{}개 스탬프 적립 완료 (stampBookId={})", toSaveNow, currentBook.getStampBookId());

            if (currentCount + toSaveNow == max) {
                currentBook.markAsCompleted();
                log.info("스탬프북이 꽉 찼습니다. 완료 처리 완료 (stampBookId={})", currentBook.getStampBookId());
            }

            stampsToAdd -= toSaveNow;
        }

        log.info("[스탬프 적립 종료] userId={}, cafeId={}, originallyRequested={}", userId, cafeId, count);
    }
}
