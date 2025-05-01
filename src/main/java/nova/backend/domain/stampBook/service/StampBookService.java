package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StampBookService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);
        return stampBooks.stream()
                .map(stampBook -> {
                    int current = stampRepository.countByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampBookResponseDTO.fromEntity(stampBook, current);
                })
                .toList();
    }

    @Transactional
    public StampBookResponseDTO createStampBook(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 1. 기존 스탬프북 조회
        List<StampBook> existing = stampBookRepository.findByUser_UserIdAndCafe_CafeId(userId, cafeId);

        for (StampBook sb : existing) {
            if (!sb.isCompleted()) {
                // 2. 미완료된 스탬프북이 하나라도 있다면 예외
                throw new BusinessException(ErrorCode.STAMPBOOK_ALREADY_EXISTS);
            }
        }

        // 3. 없거나 모두 완료된 경우 새로 생성
        StampBook newStampBook = StampBook.builder()
                .user(user)
                .cafe(cafe)
                .isCompleted(false)
                .rewardClaimed(false)
                .inHome(false)
                .build();

        stampBookRepository.save(newStampBook);
        return StampBookResponseDTO.fromEntity(newStampBook, 0);
    }

    @Transactional
    public StampBook getOrCreateValidStampBook(User user, Cafe cafe) {
        return stampBookRepository
                .findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(user.getUserId(), cafe.getCafeId())
                .orElseGet(() -> stampBookRepository.save(
                        StampBook.builder()
                                .user(user)
                                .cafe(cafe)
                                .isCompleted(false)
                                .rewardClaimed(false)
                                .inHome(false)
                                .build()
                ));
    }

    @Transactional
    public String convertStampBookToReward(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if (!stampBook.isCompleted()) {
            throw new BusinessException(ErrorCode.STAMPBOOK_NOT_COMPLETED);
        }

        if (stampBook.isRewardClaimed()) {
            throw new BusinessException(ErrorCode.REWARD_ALREADY_CLAIMED);
        }

        stampBook.convertToReward();

        String reward = stampBook.getCafe().getRewardDescription();
        if (reward == null || reward.isBlank()) {
            reward = "카페에서 기본으로 설정된 리워드가 없습니다.";
        }

        return reward;
    }

    @Transactional
    public int useRewardsByQrCode(String qrCodeValue, int count) {
        User user = userRepository.findByQrCodeValue(qrCodeValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<StampBook> usableStampBooks = stampBookRepository
                .findByUser_UserIdAndIsCompletedTrueAndRewardClaimedTrueAndUsedFalse(user.getUserId());

        if (usableStampBooks.size() < count) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_REWARDS);
        }

        // 모두 성공해야만 count 리턴
        for (int i = 0; i < count; i++) {
            StampBook stampBook = usableStampBooks.get(i);

            if (!stampBook.isCompleted()) {
                throw new BusinessException(ErrorCode.STAMPBOOK_NOT_COMPLETED);
            }
            if (!stampBook.isRewardClaimed()) {
                throw new BusinessException(ErrorCode.REWARD_NOT_CLAIMED);
            }
            if (stampBook.isUsed()) {
                throw new BusinessException(ErrorCode.REWARD_ALREADY_USED);
            }

            stampBook.useReward();
        }

        return count;
    }


}


