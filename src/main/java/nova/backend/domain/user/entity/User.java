package nova.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.global.common.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialId;

    @Column
    private String profileImageUrl;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String qrCodeValue;

    @ColumnDefault("false")
    private boolean isDeleted;

    public void delete() {this.isDeleted = true;}

    public void updateUser(User user){
        if (user.getProfileImageUrl() != null) this.profileImageUrl = user.getProfileImageUrl();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy = "user")
    private List<StampBook> stampBooks;
}
