package nova.backend.domain.stamp.repository;

import nova.backend.domain.stamp.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    int countByStampBook_StampBookId(Long stampBookId);
    List<Stamp> findByStampBook_StampBookId(Long stampBookId);

}
