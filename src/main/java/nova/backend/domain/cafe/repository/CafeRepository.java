package nova.backend.domain.cafe.repository;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    List<Cafe> findByRegistrationStatus(CafeRegistrationStatus status);

    List<Cafe> findByOwner_UserId(Long ownerId);

    @Query("""
        SELECT cs.cafe
        FROM CafeStaff cs
        WHERE cs.user.userId = :userId
    """)
    List<Cafe> findAllByUserId(Long userId);

    // 인기 카페 상위 10개 (스탬프북 수 기준 정렬)
    @Query(value = """
        SELECT c.*, COUNT(sb.stamp_book_id) AS download_count
        FROM cafe c
        LEFT JOIN stamp_book sb ON c.cafe_id = sb.cafe_id
        WHERE c.registration_status = 'APPROVED'
        GROUP BY c.cafe_id
        ORDER BY download_count DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTop10CafesByStampBookCountNative();
}
