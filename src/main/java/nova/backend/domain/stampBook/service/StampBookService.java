package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StampBookService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserIdAndUsedFalse(userId);
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
    public int useRewardsByQrCodeForCafe(Long currentUserId, Long cafeId, String qrCodeValue, int count) {
        // 현재 로그인한 사용자 조회 (owner 또는 staff)
        User staffUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // cafeId로 카페 조회
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // QR 코드로 대상 사용자 조회 (스탬프 적립 주인)
        User targetUser = userRepository.findByQrCodeValue(qrCodeValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 대상 사용자의 사용 가능한 리워드 스탬프북 리스트
        List<StampBook> targetStampBooks = stampBookRepository
                .findByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalseOrderByCreatedAtAsc(
                        targetUser.getUserId(), cafe.getCafeId()
                );

        if (targetStampBooks.size() < count) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_REWARDS);
        }

        // count만큼 사용 처리
        for (int i = 0; i < count; i++) {
            targetStampBooks.get(i).useReward();
        }
        stampBookRepository.saveAll(targetStampBooks.subList(0, count));

        return count;
    }


    @Transactional
    public void addStampBookToHome(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        if (stampBook.isUsed()) {
            throw new BusinessException(ErrorCode.ALREADY_USED_STAMPBOOK);
        }

        stampBook.toggleInHome(true);
    }


    @Transactional
    public void removeStampBookFromHome(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        stampBook.toggleInHome(false);
    }

    // 메인페이지용 스탬프북 조회 (inhome)
    @Transactional(readOnly = true)
    public List<StampBookResponseDTO> getHomeStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserIdAndInHomeTrueAndUsedFalse(userId);
        return stampBooks.stream()
                .map(stampBook -> {
                    int current = stampRepository.countByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampBookResponseDTO.fromEntity(stampBook, current);
                })
                .toList();
    }

}


