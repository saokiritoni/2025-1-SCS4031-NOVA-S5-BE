package nova.backend.domain.cafe.dto.request;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.StampBookDesign;

public record StampBookDesignCreateRequestDTO(
        String stampBookName,
        String cafeIntroduction,
        String conceptIntroduction,
        String rewardDescription,
        String designJson,
        boolean exposed,
        String frontCafeName,
        String backCafeName,
        String backImageUrl
) {
    public StampBookDesign toEntity(Cafe cafe) {
        return StampBookDesign.builder()
                .cafe(cafe)
                .stampBookName(stampBookName)
                .cafeIntroduction(cafeIntroduction)
                .conceptIntroduction(conceptIntroduction)
                .rewardDescription(rewardDescription)
                .designJson(designJson)
                .exposed(exposed)
                .frontCafeName(frontCafeName)
                .backCafeName(backCafeName)
                .backImageUrl(backImageUrl)
                .build();
    }
}

