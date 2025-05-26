package nova.backend.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.user.entity.User;
import nova.backend.global.common.BaseTimeEntity;

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
    private ParticipationStatus participationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private nova.backend.domain.challenge.entity.ParticipationStatus challengeStatus;

    @Column(nullable = false)
    private int completedCount = 0;

    public void incrementCompletedCount(int successThreshold) {
        this.completedCount++;
        this.setUpdatedAt(LocalDateTime.now());
        if (this.completedCount >= successThreshold) {
            this.challengeStatus = ParticipationStatus.COMPLETED;
        }
    }

}

