package nova.backend.domain.cafe.repository;

import nova.backend.domain.cafe.entity.StampBookDesign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampBookDesignRepository extends JpaRepository<StampBookDesign, Long> {
    /****
 * Checks if a StampBookDesign exists for the specified cafe ID.
 *
 * @param cafeId the unique identifier of the cafe
 * @return true if a StampBookDesign is associated with the given cafe ID, false otherwise
 */
boolean existsByCafe_CafeId(Long cafeId);

}
