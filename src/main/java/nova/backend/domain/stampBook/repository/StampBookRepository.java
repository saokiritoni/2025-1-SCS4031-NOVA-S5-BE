package nova.backend.domain.stampBook.repository;

import nova.backend.domain.stampBook.entity.StampBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StampBookRepository extends JpaRepository<StampBook, Long> {

    List<StampBook> findByUser_UserId(Long userId);
    Optional<StampBook> findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(Long userId, Long cafeId);
    List<StampBook> findByUser_UserIdAndCafe_CafeId(Long userId, Long cafeId);


}
