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

    /****
 * Retrieves all StampBook entities associated with the specified user ID.
 *
 * @param userId the unique identifier of the user
 * @return a list of StampBook records belonging to the user
 */
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
    /****
 * Retrieves all unused StampBook records for the specified user.
 *
 * @param userId the ID of the user whose unused StampBooks are to be retrieved
 * @return a list of unused StampBook entities for the given user
 */
List<StampBook> findByUser_UserIdAndUsedFalse(Long userId);
    /****
 * Counts the number of stamp books for a user and cafe where the reward has been claimed but not used.
 *
 * @param userId the ID of the user
 * @param cafeId the ID of the cafe
 * @return the count of stamp books with claimed rewards that have not been used
 */
int countByUser_UserIdAndCafe_CafeIdAndRewardClaimedTrueAndUsedFalse(Long userId, Long cafeId);
    /****
 * Counts the number of stamp books for a user where the reward has been claimed but not used.
 *
 * @param userId the ID of the user
 * @return the count of stamp books with claimed rewards that are unused
 */
int countByUser_UserIdAndRewardClaimedTrueAndUsedFalse(Long userId);
    /**
     * Retrieves the current incomplete StampBook for a specific user and cafe with a pessimistic write lock.
     *
     * @param userId the ID of the user
     * @param cafeId the ID of the cafe
     * @return an Optional containing the incomplete StampBook if found, or empty if none exists
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sb FROM StampBook sb WHERE sb.user.userId = :userId AND sb.cafe.cafeId = :cafeId AND sb.isCompleted = false")
    Optional<StampBook> findCurrentStampBookForUpdate(Long userId, Long cafeId);
    /****
 * Counts the number of completed stamp books for a user and cafe where the reward has not been claimed.
 *
 * @param userId the ID of the user
 * @param cafeId the ID of the cafe
 * @return the count of completed, unclaimed stamp books for the specified user and cafe
 */
int countByUser_UserIdAndCafe_CafeIdAndIsCompletedTrueAndRewardClaimedFalse(Long userId, Long cafeId);

}
