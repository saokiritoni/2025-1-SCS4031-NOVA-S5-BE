package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.StampBookDesignBasicDTO;
import nova.backend.domain.cafe.entity.Cafe;

public record CafeDesignOverviewDTO(
        @JsonUnwrapped
        CafeBasicInfoDTO basicInfo,
        @JsonUnwrapped
        StampBookDesignBasicDTO designInfo
) {
    public static CafeDesignOverviewDTO fromEntity(Cafe cafe) {
        return new CafeDesignOverviewDTO(
                CafeBasicInfoDTO.from(cafe),
                StampBookDesignBasicDTO.from(cafe)
        );
    }
}
