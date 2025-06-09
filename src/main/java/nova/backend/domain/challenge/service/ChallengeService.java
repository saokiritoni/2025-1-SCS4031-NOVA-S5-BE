package nova.backend.domain.challenge.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.dto.common.ChallengeBaseDTO;
import nova.backend.domain.challenge.dto.response.ChallengeSummaryDTO;
import nova.backend.domain.challenge.entity.Challenge;
import nova.backend.domain.challenge.entity.ChallengeParticipation;
import nova.backend.domain.challenge.entity.status.ChallengeStatus;
import nova.backend.domain.challenge.entity.status.ParticipationStatus;
import nova.backend.domain.challenge.repository.ChallengeParticipationRepository;
import nova.backend.domain.challenge.repository.ChallengeRepository;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository participationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void withdrawChallenge(Long challengeId, Long userId) {
        ChallengeParticipation participation = participationRepository
                .findByChallenge_ChallengeIdAndUser_UserId(challengeId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_PARTICIPATION_NOT_FOUND));

        if (!participation.isInProgress()) {
            throw new BusinessException(ErrorCode.INVALID_CHALLENGE_STATUS);
        }

        participation.cancelParticipation();
    }

    @Transactional(readOnly = true)
    public List<ChallengeSummaryDTO> getAvailableChallenges() {
        return challengeRepository.findAllAvailable().stream()
                .map(challenge -> new ChallengeSummaryDTO(ChallengeBaseDTO.fromEntity(challenge)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChallengeSummaryDTO> getMyChallenges(Long userId, ChallengeStatus filterStatus) {
        List<ChallengeParticipation> participations = participationRepository.findByUser_UserId(userId);

        return participations.stream()
                .filter(p ->
                        p.getParticipationStatus() == ParticipationStatus.IN_PROGRESS &&
                                p.getChallengeStatus() == filterStatus
                )
                .map(p -> new ChallengeSummaryDTO(ChallengeBaseDTO.fromEntity(p.getChallenge())))
                .toList();
    }


    @Transactional(readOnly = true)
    public ChallengeBaseDTO getChallengeDetail(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND));

        return ChallengeBaseDTO.fromEntity(challenge);
    }
}
