package nova.backend.domain.cafe.dto.response;

public record StampBookDesignResponseDTO(
        Long designId,
        String designJson,
        boolean exposed,
        String stampBookName,
        String cafeIntroduction,
        String conceptIntroduction,
        String rewardDescription
) {
    public static StampBookDesignResponseDTO fromEntity(nova.backend.domain.cafe.entity.StampBookDesign entity) {
        return new StampBookDesignResponseDTO(
                entity.getDesignId(),
                entity.getDesignJson(),
                entity.isExposed(),
                entity.getStampBookName(),
                entity.getCafeIntroduction(),
                entity.getConceptIntroduction(),
                entity.getRewardDescription()
        );
    }
}
