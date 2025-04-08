package nova.backend.domain.stampBook.repository;

import nova.backend.domain.stampBook.entity.StampBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StampBookRepository extends JpaRepository<StampBook, Long> {
    List<StampBook> findByUser_UserId(Long userId);
}

