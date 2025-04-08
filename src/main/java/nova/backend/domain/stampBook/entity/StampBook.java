package nova.backend.domain.stampBook.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.user.entity.User;
import nova.backend.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StampBook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampBookId;

    @Column(nullable = false)
    private boolean isCompleted = false;

    @Column(nullable = false)
    private boolean rewardClaimed = false;

    @Column(nullable = false)
    private boolean inHome = false;

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void claimReward() {
        this.rewardClaimed = true;
    }

    public void toggleInHome(boolean value) {
        this.inHome = value;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;
}
