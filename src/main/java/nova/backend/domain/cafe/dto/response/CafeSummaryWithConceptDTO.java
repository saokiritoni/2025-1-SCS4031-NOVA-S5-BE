package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.CafeOpenHourDTO;
import nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;

import java.util.List;

public record CafeSummaryWithConceptDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        String conceptIntroduction,
        List<CafeOpenHourDTO> openHours
) {
    public static CafeSummaryWithConceptDTO from(Cafe cafe) {
        return new CafeSummaryWithConceptDTO(
                CafeBasicInfoDTO.from(cafe),
                cafe.getConceptIntroduction(),
                CafeOperatingInfoDTO.openHoursFrom(cafe)
        );
    }
}
