package nova.backend.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StampBookDesign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long designId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String designJson;

    @Column(nullable = false)
    private boolean exposed; // 현재 노출 중인지 여부

    public void expose() { this.exposed = true; }
    public void unexpose() { this.exposed = false; }
    public boolean isExposed() { return this.exposed; }
}
