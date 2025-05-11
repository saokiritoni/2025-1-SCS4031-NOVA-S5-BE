package nova.backend.domain.cafe.dto.response;

import lombok.Builder;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

@Builder
public record MyCafeSimpleResponseDTO(
        Long cafeId,
        String cafeName,
        CafeRegistrationStatus registrationStatus,
        boolean isSelected
) {
    public static MyCafeSimpleResponseDTO from(Cafe cafe, boolean isSelected) {
        return new MyCafeSimpleResponseDTO(
                cafe.getCafeId(),
                cafe.getCafeName(),
                cafe.getRegistrationStatus(),
                isSelected
        );
    }
}
