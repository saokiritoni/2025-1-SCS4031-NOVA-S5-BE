package nova.backend.domain.user.repository;

import nova.backend.domain.user.entity.SocialType;
import nova.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByQrCodeValue(String qrCodeValue);
}
