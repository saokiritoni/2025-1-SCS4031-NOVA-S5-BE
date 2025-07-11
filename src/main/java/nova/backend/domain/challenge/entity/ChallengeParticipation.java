package nova.backend.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.challenge.entity.status.ChallengeStatus;
import nova.backend.domain.challenge.entity.status.ParticipationStatus;
import nova.backend.domain.user.entity.User;
import nova.backend.global.common.BaseTimeEntity;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChallengeParticipation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus participationStatus; // 사용자의 참여 의사

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    @Column(nullable = false)
    private int completedCount = 0;

    public static ChallengeParticipation createNew(Challenge challenge, User user) {
        return ChallengeParticipation.builder()
                .challenge(challenge)
                .user(user)
                .participationStatus(ParticipationStatus.IN_PROGRESS)
                .challengeStatus(ChallengeStatus.IN_PROGRESS)
                .completedCount(0)
                .build();
    }

    public void updateCompletedCount(int totalCount) {
        this.completedCount = totalCount;
        this.setUpdatedAt(LocalDateTime.now());

        if (this.completedCount >= this.challenge.getSuccessCount()) {
            this.challengeStatus = ChallengeStatus.COMPLETED;
        } else {
            this.challengeStatus = ChallengeStatus.IN_PROGRESS;
        }
    }

    public boolean isInProgress() {
        return this.challengeStatus == ChallengeStatus.IN_PROGRESS;
    }

    public void cancelParticipation() {
        this.participationStatus = ParticipationStatus.CANCELED;
    }

    public void rewardChallenge() {
        if (this.challengeStatus != ChallengeStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_CHALLENGE_STATUS);
        }
        this.challengeStatus = ChallengeStatus.REWARDED;
    }
}
