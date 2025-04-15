package nova.backend.domain.stamp.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stamp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stampId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stamp_book_id")
    private StampBook stampBook;
}
