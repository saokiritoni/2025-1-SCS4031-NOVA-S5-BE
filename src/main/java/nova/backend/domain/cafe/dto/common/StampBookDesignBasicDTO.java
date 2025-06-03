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
        CharacterType characterType,
        String frontCafeName,
        String backCafeName,
        String backImageUrl
) {
    public static StampBookDesignBasicDTO from(StampBookDesign design) {
        if (design == null) {
            return new StampBookDesignBasicDTO(null, null, null, null, null, null, null, null, null);
        }

        Cafe cafe = design.getCafe();

        return new StampBookDesignBasicDTO(
                design.getStampBookName(),
                design.getCafeIntroduction(),
                design.getConceptIntroduction(),
                design.getRewardDescription(),
                design.getDesignJson(),
                cafe != null ? cafe.getCharacterType() : null,
                design.getFrontCafeName(),
                design.getBackCafeName(),
                design.getBackImageUrl()
        );
    }
}
