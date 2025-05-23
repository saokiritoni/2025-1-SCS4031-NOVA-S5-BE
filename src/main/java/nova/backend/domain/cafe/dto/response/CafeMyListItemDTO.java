package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

public record CafeMyListItemDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        CafeRegistrationStatus registrationStatus,
        boolean isSelected
) {
    public static CafeMyListItemDTO from(Cafe cafe, boolean isSelected) {
        return new CafeMyListItemDTO(
                CafeBasicInfoDTO.from(cafe),
                cafe.getRegistrationStatus(),
                isSelected
        );
    }
}
