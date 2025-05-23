package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;

public record CafeSummaryResponseDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        boolean isOpenNow,
        int downloadCount
) {
    public static CafeSummaryResponseDTO from(CafeWithDownloadCountDTO dto) {
        Cafe cafe = dto.cafe();
        int downloadCount = dto.downloadCount().intValue();

        return new CafeSummaryResponseDTO(
                CafeBasicInfoDTO.from(cafe),
                CafeOperatingInfoDTO.checkIsOpenNow(cafe),
                downloadCount
        );
    }
}
