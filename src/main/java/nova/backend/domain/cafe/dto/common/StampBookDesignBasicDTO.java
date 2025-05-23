package nova.backend.domain.cafe.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;

public record StampBookDesignBasicDTO(
        String stampBookName,
        String cafeIntroduction,
        String conceptIntroduction,
        String rewardDescription,
        boolean isCustomized,
        String stampBookDesignJson,
        CharacterType characterType
) {
    public static StampBookDesignBasicDTO from(Cafe cafe) {
        return new StampBookDesignBasicDTO(
                cafe.getStampBookName(),
                cafe.getCafeIntroduction(),
                cafe.getConceptIntroduction(),
                cafe.getRewardDescription(),
                cafe.isCustomized(),
                cafe.getStampBookDesignJson(),
                cafe.getCharacterType()
        );
    }
}
