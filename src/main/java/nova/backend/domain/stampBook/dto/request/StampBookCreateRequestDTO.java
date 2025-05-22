package nova.backend.domain.stampBook.dto.request;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.user.entity.User;

public record StampBookCreateRequestDTO(
        Long cafeId
) {
    public StampBook toEntity(User user, Cafe cafe) {
        return StampBook.builder()
                .user(user)
                .cafe(cafe)
                .isCompleted(false)
                .rewardClaimed(false)
                .inHome(false)
                .build();
    }
}
