package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;

public record CafeSummaryWithConceptDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        String conceptIntroduction
) {
    public static CafeSummaryWithConceptDTO from(Cafe cafe) {
        return new CafeSummaryWithConceptDTO(
                CafeBasicInfoDTO.from(cafe),
                cafe.getConceptIntroduction()
        );
    }
}
