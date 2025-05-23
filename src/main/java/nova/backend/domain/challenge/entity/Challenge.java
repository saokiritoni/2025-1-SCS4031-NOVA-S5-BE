package nova.backend.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.global.common.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    @Column(nullable = false)
    private String reward;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int successCount = 10;

    public int getParticipantCount() {
        return participations.size();
    }

    public int getCompletedCount() {
        return (int) participations.stream()
                .filter(p -> p.getChallengeStatus() == ChallengeStatus.COMPLETED)
                .count();
    }

    public int getCanceledCount() {
        return (int) participations.stream()
                .filter(p -> p.getChallengeStatus() == ChallengeStatus.CANCELED)
                .count();
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeParticipation> participations = new ArrayList<>();
}

