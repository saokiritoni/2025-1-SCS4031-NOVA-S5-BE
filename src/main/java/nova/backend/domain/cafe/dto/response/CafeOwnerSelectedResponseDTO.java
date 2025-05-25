package nova.backend.domain.cafe.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import nova.backend.domain.cafe.dto.common.CafeBasicInfoDTO;
import nova.backend.domain.cafe.dto.common.CafeOpenHourDTO;
import nova.backend.domain.cafe.dto.common.CafeSpecialDayDTO;
import nova.backend.domain.cafe.dto.common.CafeOperatingInfoDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

import java.util.List;

public record CafeOwnerSelectedResponseDTO(
        @JsonUnwrapped CafeBasicInfoDTO basicInfo,
        boolean isOpenNow,
        CafeRegistrationStatus registrationStatus,
        List<CafeOpenHourDTO> openHours,
        List<CafeSpecialDayDTO> specialDays,
        String rewardDescription
) {
    public static CafeOwnerSelectedResponseDTO fromEntity(Cafe cafe) {
        return new CafeOwnerSelectedResponseDTO(
                CafeBasicInfoDTO.from(cafe),
                CafeOperatingInfoDTO.checkIsOpenNow(cafe),
                cafe.getRegistrationStatus(),
                CafeOperatingInfoDTO.openHoursFrom(cafe),
                CafeOperatingInfoDTO.specialDaysFrom(cafe),
                cafe.getRewardDescription()
        );
    }
}
