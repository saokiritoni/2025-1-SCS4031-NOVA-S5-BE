package nova.backend.domain.cafe.dto.request;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import nova.backend.domain.cafe.entity.CharacterType;
import nova.backend.domain.user.entity.User;

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
) {
    public Cafe toEntity(User owner) {
        return Cafe.builder()
                .cafeName(cafeName)
                .branchName(branchName)
                .ownerName(ownerName)
                .ownerPhone(ownerPhone)
                .businessNumber(businessNumber)
                .latitude(latitude)
                .longitude(longitude)
                .maxStampCount(maxStampCount)
                .characterType(characterType)
                .rewardDescription(rewardDescription)
                .registrationStatus(CafeRegistrationStatus.REQUESTED)
                .owner(owner)
                .build();
    }
}