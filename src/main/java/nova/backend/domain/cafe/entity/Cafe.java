package nova.backend.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.stampBook.entity.StampBook;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeId;

    @Column(nullable = false)
    private String cafeName;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String cafePhone;

    @Column(nullable = false)
    private Integer maxStampCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacterType characterType;

    @Column(nullable = false)
    private String rewardDescription;  // e.g. 아메리카노 한 잔

    @OneToMany(mappedBy = "cafe")
    private List<StampBook> stampBooks;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeOpenHour> openHours = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeSpecialDay> specialDays = new ArrayList<>();
}
