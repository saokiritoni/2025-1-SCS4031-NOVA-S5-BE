package nova.backend.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CafeSpecialDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeSpecialDayId;

    @Column(name = "special_date", nullable = false)
    private LocalDate specialDate;

    @Column(nullable = false)
    private boolean isOpen;

    private LocalTime openTime;
    private LocalTime closeTime;
    private LocalTime lastOrder;
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

}