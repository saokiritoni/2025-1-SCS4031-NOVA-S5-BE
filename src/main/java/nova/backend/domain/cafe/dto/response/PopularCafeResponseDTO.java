package nova.backend.domain.cafe.dto.response;

import lombok.Builder;

@Builder
public record PopularCafeResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        Integer downloadCount
) {
    public static PopularCafeResponseDTO from(CafeWithDownloadCountDTO dto) {
        return PopularCafeResponseDTO.builder()
                .cafeId(dto.cafe().getCafeId())
                .cafeName(dto.cafe().getCafeName())
                .latitude(dto.cafe().getLatitude())
                .longitude(dto.cafe().getLongitude())
                .downloadCount(dto.downloadCount().intValue())
                .build();
    }
}
