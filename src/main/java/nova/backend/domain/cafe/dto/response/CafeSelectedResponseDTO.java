package nova.backend.domain.cafe.dto.response;

import lombok.Builder;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

import java.util.List;

@Builder
public record CafeSelectedResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        String cafePhone,
        Integer maxStampCount,
        boolean isOpenNow,
        CafeRegistrationStatus registrationStatus,
        List<CafeListResponseDTO.CafeOpenHourDTO> openHours,
        List<CafeListResponseDTO.CafeSpecialDayDTO> specialDays
) {
    public static CafeSelectedResponseDTO fromEntity(Cafe cafe) {
        CafeListResponseDTO base = CafeListResponseDTO.fromEntity(cafe);
        return new CafeSelectedResponseDTO(
                base.cafeId(),
                base.cafeName(),
                base.latitude(),
                base.longitude(),
                base.cafePhone(),
                base.maxStampCount(),
                base.isOpenNow(),
                cafe.getRegistrationStatus(),
                base.openHours(),
                base.specialDays()
        );
    }
}
