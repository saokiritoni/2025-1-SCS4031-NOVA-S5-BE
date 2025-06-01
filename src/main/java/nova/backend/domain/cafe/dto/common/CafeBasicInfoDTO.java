package nova.backend.domain.cafe.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;

public record CafeBasicInfoDTO(
        Long cafeId,
        String cafeName,
        String branchName,
        String cafeIntroduction,
        Integer maxStampCount,
        Double latitude,
        Double longitude,
        String roadAddress,
        String cafePhone,
        CharacterType characterType,
        String cafeUrl

) {
    public static CafeBasicInfoDTO from(Cafe cafe) {
        return new CafeBasicInfoDTO(
                cafe.getCafeId(),
                cafe.getCafeName(),
                cafe.getBranchName(),
                cafe.getCafeIntroduction(),
                cafe.getMaxStampCount(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getRoadAddress(),
                cafe.getCafePhone(),
                cafe.getCharacterType(),
                cafe.getCafeUrl()
        );
    }
}
