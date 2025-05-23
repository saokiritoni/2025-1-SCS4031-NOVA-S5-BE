package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.StampBookDesignInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;

public record CafeDesignDetailResponseDTO(
        @JsonUnwrapped
        CafeBasicInfoDTO basicInfo,

        String branchName,

        @JsonUnwrapped
        StampBookDesignInfoDTO designInfo
) {
    public static CafeDesignDetailResponseDTO fromEntity(Cafe cafe) {
        return new CafeDesignDetailResponseDTO(
                CafeBasicInfoDTO.from(cafe),
                cafe.getBranchName(),
                StampBookDesignInfoDTO.from(cafe)
        );
    }
}
