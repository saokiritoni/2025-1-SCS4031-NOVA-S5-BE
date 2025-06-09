package nova.backend.domain.cafe.repository;

import nova.backend.domain.cafe.entity.StampBookDesign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampBookDesignRepository extends JpaRepository<StampBookDesign, Long> {
    boolean existsByCafe_CafeId(Long cafeId);

}
