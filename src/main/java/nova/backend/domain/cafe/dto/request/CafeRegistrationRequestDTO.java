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
        String cafePhone,
        String businessNumber,
        Double latitude,
        Double longitude,
        String roadAddress,
        Integer maxStampCount,
        CharacterType characterType,
        String rewardDescription,
        String cafeUrl
) {
    public Cafe toEntity(User owner) {
        return Cafe.builder()
                .cafeName(cafeName)
                .branchName(branchName)
                .ownerName(ownerName)
                .ownerPhone(ownerPhone)
                .cafePhone(cafePhone)
                .businessNumber(businessNumber)
                .latitude(latitude)
                .longitude(longitude)
                .roadAddress(roadAddress)
                .maxStampCount(maxStampCount)
                .characterType(characterType)
                .cafeUrl(cafeUrl)
                .registrationStatus(CafeRegistrationStatus.REQUESTED)
                .owner(owner)
                .build();
    }
}
