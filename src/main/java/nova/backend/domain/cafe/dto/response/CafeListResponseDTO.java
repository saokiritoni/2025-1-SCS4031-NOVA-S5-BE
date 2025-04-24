package nova.backend.domain.cafe.dto.response;

import nova.backend.domain.cafe.entity.CharacterType;

public record CafeListResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        String cafePhone,
        Integer maxStampCount,
        CharacterType character
) {
}
