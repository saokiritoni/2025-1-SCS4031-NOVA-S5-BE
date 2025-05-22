package nova.backend.domain.cafe.dto.response;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CharacterType;

public record CafeDetailResponseDTO(
        Long cafeId,
        String cafeName,
        String branchName,
        String stampBookName,
        String cafeIntroduction,
        String conceptIntroduction,
        String rewardDescription,
        int maxStampCount,
        CharacterType characterType,
        boolean isCustomized,
        String stampBookDesignJson
) {
    public static CafeDetailResponseDTO fromEntity(Cafe cafe) {
        return new CafeDetailResponseDTO(
                cafe.getCafeId(),
                cafe.getCafeName(),
                cafe.getBranchName(),
                cafe.getStampBookName(),
                cafe.getCafeIntroduction(),
                cafe.getConceptIntroduction(),
                cafe.getRewardDescription(),
                cafe.getMaxStampCount(),
                cafe.getCharacterType(),
                cafe.isCustomized(),
                cafe.getStampBookDesignJson()
        );
    }
}