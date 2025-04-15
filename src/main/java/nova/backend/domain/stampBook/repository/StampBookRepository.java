package nova.backend.domain.stampBook.repository;

import nova.backend.domain.stampBook.entity.StampBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StampBookRepository extends JpaRepository<StampBook, Long> {

    List<StampBook> findByUser_UserId(Long userId);
    boolean existsByUser_UserIdAndCafe_CafeId(Long userId, Long cafeId);
    Optional<StampBook> findByUser_UserIdAndCafe_CafeId(Long userId, Long cafeId);
    Optional<StampBook> findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(Long userId, Long cafeId);

}
