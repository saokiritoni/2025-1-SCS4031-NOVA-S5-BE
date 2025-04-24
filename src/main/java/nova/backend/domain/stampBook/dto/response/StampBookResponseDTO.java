package nova.backend.domain.stampBook.dto.response;


import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;
import nova.backend.domain.stampBook.entity.StampBook;

import java.time.LocalDateTime;

public record StampBookResponseDTO(
        Long stampBookId,
        Long cafeId,
        String cafeName,
        CharacterType characterType,
        boolean isCompleted,
        boolean rewardClaimed,
        boolean inHome,
        LocalDateTime createdAt
) {
    public static StampBookResponseDTO fromEntity(StampBook stampBook) {
        Cafe cafe = stampBook.getCafe();
        return new StampBookResponseDTO(
                stampBook.getStampBookId(),
                cafe.getCafeId(),
                cafe.getCafeName(),
                stampBook.getCafe().getCharacterType(),
                stampBook.isCompleted(),
                stampBook.isRewardClaimed(),
                stampBook.isInHome(),
                stampBook.getCreatedAt()
        );
    }
}
