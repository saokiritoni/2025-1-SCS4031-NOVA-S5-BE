package nova.backend.domain.cafe.repository;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findByRegistrationStatus(CafeRegistrationStatus status);
    List<Cafe> findByOwner_UserId(Long ownerId);
}
