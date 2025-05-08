package nova.backend.domain.cafe.repository;

import nova.backend.domain.cafe.entity.CafeStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeStaffRepository extends JpaRepository<CafeStaff, Long> {

    // 카페와 유저 기준으로 staff 존재 여부
    boolean existsByCafe_CafeIdAndUser_UserId(Long cafeId, Long userId);

    // 유저의 모든 소속 카페 정보
    List<CafeStaff> findByUser_UserId(Long userId);

    // 카페의 모든 staff 정보
    List<CafeStaff> findByCafe_CafeId(Long cafeId);

    // 카페 + 유저 → 특정 CafeStaff 엔티티 반환
    Optional<CafeStaff> findByCafe_CafeIdAndUser_UserId(Long cafeId, Long userId);
}
