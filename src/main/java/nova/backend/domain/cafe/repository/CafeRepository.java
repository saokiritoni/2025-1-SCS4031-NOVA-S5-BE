package nova.backend.domain.cafe.repository;

import io.lettuce.core.dynamic.annotation.Param;
import nova.backend.domain.cafe.dto.response.CafeWithDownloadCountDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {


    @Query("""
SELECT c
FROM Cafe c
WHERE c.registrationStatus = :status
AND EXISTS (
    SELECT d FROM StampBookDesign d
    WHERE d.cafe = c AND d.exposed = true
)
""")
    List<Cafe> findByRegistrationStatusWithExposedDesign(@Param("status") CafeRegistrationStatus status);

    List<Cafe> findByRegistrationStatus(CafeRegistrationStatus status);

    List<Cafe> findByOwner_UserId(Long ownerId);

    @Query("""
        SELECT cs.cafe
        FROM CafeStaff cs
        WHERE cs.user.userId = :userId
    """)
    List<Cafe> findAllByUserId(Long userId);

    // 인기 카페 상위 10개 (스탬프북 수 기준 정렬)
    @Query("""
SELECT new nova.backend.domain.cafe.dto.response.CafeWithDownloadCountDTO(c, COUNT(sb))
FROM Cafe c
LEFT JOIN c.stampBooks sb
WHERE c.registrationStatus = nova.backend.domain.cafe.entity.CafeRegistrationStatus.APPROVED
  AND EXISTS (
    SELECT d FROM StampBookDesign d
    WHERE d.cafe = c AND d.exposed = true
  )
GROUP BY c
ORDER BY COUNT(sb) DESC
""")
    List<CafeWithDownloadCountDTO> findTop10CafesByStampBookCount(Pageable pageable);


}
