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
    List<Cafe> findAllByUserId(Long userId); // ✅ 추가된 메서드
}
