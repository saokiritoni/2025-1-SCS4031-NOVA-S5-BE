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
        boolean used,
        LocalDateTime createdAt,
        int currentStampCount,
        int maxStampCount,
        int remainingStampCount
) {
    public static StampBookResponseDTO fromEntity(StampBook stampBook, int currentStampCount) {
        Cafe cafe = stampBook.getCafe();
        int max = cafe.getMaxStampCount();
        int remaining = max - currentStampCount;
        return new StampBookResponseDTO(
                stampBook.getStampBookId(),
                cafe.getCafeId(),
                cafe.getCafeName(),
                stampBook.getCafe().getCharacterType(),
                stampBook.isCompleted(),
                stampBook.isRewardClaimed(),
                stampBook.isInHome(),
                stampBook.isUsed(),
                stampBook.getCreatedAt(),
                currentStampCount,
                max,
                remaining
        );
    }
}
