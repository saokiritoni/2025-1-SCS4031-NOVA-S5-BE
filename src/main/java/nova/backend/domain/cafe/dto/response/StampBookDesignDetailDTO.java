package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.StampBookDesignInfoDTO;
import nova.backend.domain.cafe.entity.StampBookDesign;

public record StampBookDesignDetailDTO(
        Long designId,
        String designJson,
        boolean exposed,
        @JsonUnwrapped StampBookDesignInfoDTO designInfo
) {
    public static StampBookDesignDetailDTO fromEntity(StampBookDesign entity) {
        return new StampBookDesignDetailDTO(
                entity.getDesignId(),
                entity.getDesignJson(),
                entity.isExposed(),
                new StampBookDesignInfoDTO(
                        entity.getStampBookName(),
                        entity.getCafeIntroduction(),
                        entity.getConceptIntroduction(),
                        entity.getRewardDescription(),
                        true, 
                        entity.getDesignJson(),
                        entity.getCafe().getCharacterType()
                )
        );
    }
}
