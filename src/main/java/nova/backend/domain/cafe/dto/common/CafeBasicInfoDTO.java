package nova.backend.domain.cafe.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;

public record CafeBasicInfoDTO(
        Long cafeId,
        String cafeName,
        Integer maxStampCount,
        Double latitude,
        Double longitude,
        String cafePhone,
        CharacterType characterType,
        String cafeIntroduction
) {
    public static CafeBasicInfoDTO from(Cafe cafe) {
        return new CafeBasicInfoDTO(
                cafe.getCafeId(),
                cafe.getCafeName(),
                cafe.getMaxStampCount(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getCafePhone(),
                cafe.getCharacterType(),
                cafe.getCafeIntroduction()
        );
    }
}
