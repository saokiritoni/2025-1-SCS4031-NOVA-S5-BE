package nova.backend.domain.stampBook.repository;

import jakarta.persistence.LockModeType;
import nova.backend.domain.stampBook.entity.StampBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StampBookRepository extends JpaRepository<StampBook, Long> {

    List<StampBook> findByUser_UserId(Long userId);
    Optional<StampBook> findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(Long userId, Long cafeId);
    List<StampBook> findByUser_UserIdAndCafe_CafeId(Long userId, Long cafeId);
    /*
    rewardClaimed = true인 스탬프북 중, 가장 오래된 것부터 가져오기
     */
    List<StampBook> findByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalseOrderByCreatedAtAsc(
            Long userId, Long cafeId
    );
    List<StampBook> findByUser_UserIdAndInHomeTrueAndUsedFalse(Long userId);
    List<StampBook> findByUser_UserIdAndUsedFalse(Long userId);
    int countByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalse(Long userId, Long cafeId);
    int countByUser_UserIdAndRewardClaimedTrueAndUsedFalse(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sb FROM StampBook sb WHERE sb.user.userId = :userId AND sb.cafe.cafeId = :cafeId AND sb.isCompleted = false")
    Optional<StampBook> findCurrentStampBookForUpdate(Long userId, Long cafeId);
    int countByUser_UserIdAndCafe_CafeIdAndIsCompletedTrueAndRewardClaimedFalse(Long userId, Long cafeId);

}
