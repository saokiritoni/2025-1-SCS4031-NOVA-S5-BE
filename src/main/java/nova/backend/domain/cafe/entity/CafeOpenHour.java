package nova.backend.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CafeOpenHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeOpenHourId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private boolean isOpen;

    @Column(nullable = true)
    private LocalTime openTime;

    @Column(nullable = true)
    private LocalTime closeTime;

    private LocalTime lastOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

}



