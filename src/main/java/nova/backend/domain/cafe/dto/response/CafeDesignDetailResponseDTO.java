package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.StampBookDesignBasicDTO;
import nova.backend.domain.cafe.entity.Cafe;

public record CafeDesignDetailResponseDTO(
        @JsonUnwrapped
        CafeBasicInfoDTO basicInfo,

        String branchName,

        @JsonUnwrapped
        StampBookDesignBasicDTO designInfo
) {
    public static CafeDesignDetailResponseDTO fromEntity(Cafe cafe) {
        return new CafeDesignDetailResponseDTO(
                CafeBasicInfoDTO.from(cafe),
                cafe.getBranchName(),
                StampBookDesignBasicDTO.from(cafe)
        );
    }
}
