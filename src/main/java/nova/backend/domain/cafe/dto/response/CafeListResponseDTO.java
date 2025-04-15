package nova.backend.domain.cafe.dto.response;

public record CafeListResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        String cafePhone,
        Integer maxStampCount
) {
}
