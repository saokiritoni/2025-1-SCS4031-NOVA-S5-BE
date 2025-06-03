package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.StampBookDesignBasicDTO;
import nova.backend.domain.cafe.entity.StampBookDesign;

public record StampBookDesignDetailDTO(
        Long designId,
        boolean exposed,
        @JsonUnwrapped StampBookDesignBasicDTO designInfo
) {
    public static StampBookDesignDetailDTO fromEntity(StampBookDesign entity) {
        return new StampBookDesignDetailDTO(
                entity.getDesignId(),
                entity.isExposed(),
                new StampBookDesignBasicDTO(
                        entity.getStampBookName(),
                        entity.getCafeIntroduction(),
                        entity.getConceptIntroduction(),
                        entity.getRewardDescription(),
                        entity.getDesignJson(),
                        entity.getCafe().getCharacterType(),
                        entity.getFrontCafeName(),
                        entity.getBackCafeName(),
                        entity.getBackImageUrl()
                )
        );
    }
}
