package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.CafeOpenHourDTO;
import nova.backend.domain.cafe.dto.common.CafeSpecialDayDTO;
import nova.backend.domain.cafe.entity.Cafe;

import java.util.List;

public record CafeDetailResponseDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        boolean isOpenNow,
        List<CafeOpenHourDTO> openHours,
        List<CafeSpecialDayDTO> specialDays,
        int downloadCount,
        StampBookDesignDetailDTO exposedDesign
) {
    public static CafeDetailResponseDTO fromEntity(Cafe cafe, int downloadCount) {
        var exposed = cafe.getExposedDesign();
        return new CafeDetailResponseDTO(
                CafeBasicInfoDTO.from(cafe),
                nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO.checkIsOpenNow(cafe),
                nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO.openHoursFrom(cafe),
                nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO.specialDaysFrom(cafe),
                downloadCount,
                exposed != null ? StampBookDesignDetailDTO.fromEntity(exposed) : null
        );
    }
}
