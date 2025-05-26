package nova.backend.domain.challenge.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.entity.Challenge;
import nova.backend.domain.challenge.entity.ChallengeParticipation;
import nova.backend.domain.challenge.entity.ParticipationStatus;
import nova.backend.domain.challenge.repository.ChallengeParticipationRepository;
import nova.backend.domain.challenge.repository.ChallengeRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffChallengeService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository participationRepository;

    public void accumulateOngoingChallengeForCafe(Long cafeId, String qrCodeValue) {
        // 1. 유저 조회
        User user = userRepository.findByQrCodeValue(qrCodeValue)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 진행 중인 챌린지 조회
        Challenge challenge = challengeRepository
                .findFirstByCafe_CafeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        cafeId, LocalDate.now(), LocalDate.now())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_NOT_FOUND));

        // 3. 참여 내역 조회 또는 자동 생성
        ChallengeParticipation participation = participationRepository
                .findByChallenge_ChallengeIdAndUser_UserId(challenge.getChallengeId(), user.getUserId())
                .orElseGet(() -> {
                    ChallengeParticipation newParticipation = ChallengeParticipation.builder()
                            .challenge(challenge)
                            .user(user)
                            .participationStatus(ParticipationStatus.IN_PROGRESS)
                            .challengeStatus(ParticipationStatus.IN_PROGRESS)
                            .completedCount(0)
                            .build();
                    return participationRepository.save(newParticipation);
                });

        // 4. 상태 확인
        if (participation.getChallengeStatus() != ParticipationStatus.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.CHALLENGE_ALREADY_COMPLETED_OR_CANCELED);
        }

        // 5. 하루에 한 번만 적립 확인
        if (hasAccumulatedToday(participation)) {
            throw new BusinessException(ErrorCode.ALREADY_ACCUMULATED_TODAY);
        }

        // 6. 적립 및 상태 변경
        participation.incrementCompletedCount(challenge.getSuccessCount());
    }

    private boolean hasAccumulatedToday(ChallengeParticipation participation) {
        if (participation.getCompletedCount() == 0) { // 처음 적립하는 경우는 통과
            return false;
        }
        LocalDate today = LocalDate.now();
        return participation.getUpdatedAt() != null &&
                participation.getUpdatedAt().toLocalDate().isEqual(today);
    }

}



