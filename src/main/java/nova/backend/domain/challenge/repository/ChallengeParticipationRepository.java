package nova.backend.domain.challenge.repository;

import nova.backend.domain.challenge.entity.ChallengeParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    Optional<ChallengeParticipation> findByChallenge_ChallengeIdAndUser_UserId(Long challengeId, Long userId);

}