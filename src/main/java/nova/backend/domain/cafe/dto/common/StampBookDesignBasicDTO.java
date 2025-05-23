package nova.backend.domain.cafe.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;
import nova.backend.domain.cafe.entity.StampBookDesign;

public record StampBookDesignBasicDTO(
        String stampBookName,
        String cafeIntroduction,
        String conceptIntroduction,
        String rewardDescription,
        String stampBookDesignJson,
        CharacterType characterType
) {
    public static StampBookDesignBasicDTO from(StampBookDesign design) {
        if (design == null) {
            return new StampBookDesignBasicDTO(null, null, null, null, null, null);
        }

        return new StampBookDesignBasicDTO(
                design.getStampBookName(),
                design.getCafeIntroduction(),
                design.getConceptIntroduction(),
                design.getRewardDescription(),
                design.getDesignJson(),
                design.getCafe() != null ? design.getCafe().getCharacterType() : null
        );
    }


}

