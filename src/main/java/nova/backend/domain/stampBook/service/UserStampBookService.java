package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.dto.response.CafeDesignOverviewDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.StampBookDesign;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.dto.response.StampBookDetailResponseDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nova.backend.global.error.ErrorCode.ACCESS_DENIED;
import static nova.backend.global.error.ErrorCode.ENTITY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserStampBookService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> all = stampBookRepository.findByUser_UserId(userId);

        // 카페 ID 기준으로 그룹핑
        Map<Long, List<StampBook>> groupedByCafe = all.stream()
                .collect(Collectors.groupingBy(sb -> sb.getCafe().getCafeId()));

        // 각 카페마다 조건에 따라 스탬프북 선택
        List<StampBook> filtered = groupedByCafe.values().stream()
                .map(list -> list.stream()
                        .filter(sb -> !sb.isCompleted()) // 미완료된 게 있으면 우선 반환
                        .findFirst()
                        .orElse(list.get(0)) // 없다면 그냥 첫 번째 반환
                )
                .toList();

        return filtered.stream()
                .map(sb -> {
                    int current = stampRepository.countByStampBook_StampBookId(sb.getStampBookId());
                    return StampBookResponseDTO.fromEntity(sb, current);
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

    /**
     * Retrieves the current incomplete stamp book for a user and cafe with a database lock, or creates a new one if none exists.
     *
     * If a concurrent creation conflict or database lock failure occurs, throws a business exception indicating concurrent stamp book creation.
     *
     * @return the existing or newly created incomplete stamp book for the user and cafe
     */
    @Transactional
    public StampBook getOrCreateValidStampBook(User user, Cafe cafe) {
        try {
            return stampBookRepository.findCurrentStampBookForUpdate(user.getUserId(), cafe.getCafeId())
                    .orElseGet(() -> {
                        stampBookRepository.flush(); // DB 상태 동기화
                        boolean exists = stampBookRepository.findCurrentStampBookForUpdate(user.getUserId(), cafe.getCafeId()).isPresent();
                        if (exists) {
                            return stampBookRepository.findCurrentStampBookForUpdate(user.getUserId(), cafe.getCafeId()).get();
                        }

                        return stampBookRepository.save(
                                StampBook.builder()
                                        .user(user)
                                        .cafe(cafe)
                                        .isCompleted(false)
                                        .rewardClaimed(false)
                                        .inHome(false)
                                        .build()
                        );
                    });
        } catch (CannotAcquireLockException e) {
            log.warn("Deadlock 또는 락 획득 실패: userId={}, cafeId={}", user.getUserId(), cafe.getCafeId(), e);
            throw new BusinessException(ErrorCode.CONCURRENT_STAMP_CREATION);
        } catch (DataAccessException e) {
            log.warn("기타 데이터 액세스 오류: userId={}, cafeId={}", user.getUserId(), cafe.getCafeId(), e);
            throw new BusinessException(ErrorCode.CONCURRENT_STAMP_CREATION);
        }
    }


    /**
     * Converts a completed stamp book into a reward for the user.
     *
     * Validates ownership, completion status, and reward claim status of the stamp book.
     * Marks the stamp book as reward-claimed and returns the associated cafe's reward description,
     * or a default message if no reward is set.
     *
     * @param userId the ID of the user requesting the reward conversion
     * @param stampBookId the ID of the stamp book to convert
     * @return the reward description for the cafe, or a default message if not set
     *
     * @throws BusinessException if the stamp book does not exist, is not owned by the user, is incomplete, or the reward has already been claimed
     */
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

    public List<StampBookResponseDTO> getMyStampBooks(Long userId) {
        return stampBookRepository.findByUser_UserIdAndUsedFalse(userId).stream()
                .map(stampBook -> {
                    int current = stampRepository.countByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampBookResponseDTO.fromEntity(stampBook, current);
                })
                .toList();
    }

    public void saveStampBookToMyList(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ACCESS_DENIED);
        }

        stampBook.toggleInHome(true);
    }

    @Transactional(readOnly = true)
    public StampBookDetailResponseDTO getStampBookDetail(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Cafe cafe = stampBook.getCafe();
        StampBookDesign design = cafe.getExposedDesign();

        if (design == null) {
            throw new BusinessException(ErrorCode.DESIGN_NOT_FOUND);
        }

        int currentStampCount = stampRepository.countByStampBook_StampBookId(stampBookId);

        int rewardAvailableCount = stampBookRepository
                .countByUser_UserIdAndCafe_CafeIdAndIsCompletedTrueAndRewardClaimedFalse(userId, cafe.getCafeId());

        return StampBookDetailResponseDTO.of(
                CafeDesignOverviewDTO.fromEntity(cafe, design),
                StampBookResponseDTO.fromEntity(stampBook, currentStampCount),
                rewardAvailableCount
        );
    }

    @Transactional
    public void deleteStampBook(Long userId, Long stampBookId) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (!stampBook.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ACCESS_DENIED);
        }

        if (stampBook.isUsed()) {
            throw new BusinessException(ErrorCode.ALREADY_USED_STAMPBOOK);
        }

        stampBookRepository.delete(stampBook);
    }





}
