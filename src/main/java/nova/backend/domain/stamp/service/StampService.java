package nova.backend.domain.stamp.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public void accumulateStamp(Long userId, Long cafeId, int count) {
        // 1. 스탬프북 찾거나 생성
        StampBook stampBook = stampBookRepository.findByUser_UserIdAndCafe_CafeId(userId, cafeId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
                    Cafe cafe = cafeRepository.findById(cafeId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
                    StampBook newBook = StampBook.builder()
                            .user(user)
                            .cafe(cafe)
                            .isCompleted(false)
                            .rewardClaimed(false)
                            .inHome(false)
                            .build();
                    return stampBookRepository.save(newBook);
                });

        // 2. 유저 본인인지 검증
        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN); // 혹은 STAMPBOOK_ACCESS_DENIED
        }

        // 3. 개수 제한 체크
        int current = stampRepository.countByStampBook_StampBookId(stampBook.getStampBookId());
        int max = stampBook.getCafe().getMaxStampCount();

        if (current + count > max) {
            throw new BusinessException(ErrorCode.STAMP_LIMIT_EXCEEDED);
        }

        // 4. 적립
        for (int i = 0; i < count; i++) {
            Stamp stamp = Stamp.builder()
                    .stampBook(stampBook)
                    .build();
            stampRepository.save(stamp);
        }

        // 5. 완료 처리
        if (current + count == max) {
            stampBook.markAsCompleted();
        }
    }
}
