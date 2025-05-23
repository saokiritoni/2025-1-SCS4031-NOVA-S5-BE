package nova.backend.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.user.entity.User;

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
    private String branchName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String ownerPhone;

    @Column(nullable = false)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CafeRegistrationStatus registrationStatus;

    @OneToMany(mappedBy = "cafe")
    private List<StampBook> stampBooks;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeOpenHour> openHours = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeSpecialDay> specialDays = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "cafe")
    private List<CafeStaff> staffList = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StampBookDesign> stampBookDesigns = new ArrayList<>();

    public boolean hasExposedDesign() {
        return getExposedDesign() != null;
    }

    public String getStampBookDesignJson() {
        StampBookDesign exposed = getExposedDesign();
        return exposed != null ? exposed.getDesignJson() : null;
    }

    public StampBookDesign getExposedDesign() {
        return stampBookDesigns.stream()
                .filter(StampBookDesign::isExposed)
                .findFirst()
                .orElse(null);
    }

    public String getRewardDescription() {
        StampBookDesign exposed = getExposedDesign();
        return exposed != null ? exposed.getRewardDescription() : null;
    }

    public String getStampBookName() {
        StampBookDesign exposed = getExposedDesign();
        return exposed != null ? exposed.getStampBookName() : null;
    }

    public String getCafeIntroduction() {
        StampBookDesign exposed = getExposedDesign();
        return exposed != null ? exposed.getCafeIntroduction() : null;
    }

    public String getConceptIntroduction() {
        StampBookDesign exposed = getExposedDesign();
        return exposed != null ? exposed.getConceptIntroduction() : null;
    }
}
