package nova.backend.domain.cafe.dto.request;

import nova.backend.domain.cafe.entity.CharacterType;

public record CafeRegistrationRequestDTO(
        String cafeName,
        String branchName,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Double latitude,
        Double longitude,
        Integer maxStampCount,
        CharacterType characterType,
        String rewardDescription
) {}


