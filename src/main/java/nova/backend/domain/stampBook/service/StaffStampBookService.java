package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nova.backend.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffStampBookService {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final StampBookRepository stampBookRepository;

    /**
     * STAFF가 유저의 QR 코드를 찍고, 해당 유저의 리워드 스탬프북을 사용 처리한다.
     */
    public int useRewardsByQrCodeForCafe(Long staffUserId, Long selectedCafeId, String qrCodeValue, int count) {

        if (selectedCafeId == null) {
            throw new BusinessException(CAFE_NOT_SELECTED);
        }

        // 스태프 유효성 검증
        User staffUser = userRepository.findById(staffUserId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        // QR 코드로 유저 찾기
        User targetUser = userRepository.findByQrCodeValue(qrCodeValue)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        List<StampBook> targetStampBooks = stampBookRepository
                .findByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalseOrderByCreatedAtAsc(
                        targetUser.getUserId(), cafe.getCafeId()
                );

        if (targetStampBooks.size() < count) {
            throw new BusinessException(NOT_ENOUGH_REWARDS);
        }

        for (int i = 0; i < count; i++) {
            targetStampBooks.get(i).useReward();
        }

        stampBookRepository.saveAll(targetStampBooks.subList(0, count));
        return count;
    }
}
