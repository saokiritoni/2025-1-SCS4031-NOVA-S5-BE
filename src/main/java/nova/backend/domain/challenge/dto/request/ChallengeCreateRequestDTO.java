package nova.backend.domain.challenge.dto.request;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.challenge.entity.Challenge;
import nova.backend.domain.challenge.entity.ChallengeType;

import java.time.LocalDate;

public record ChallengeCreateRequestDTO(
        String name,
        ChallengeType type,
        String reward,
        LocalDate startDate,
        LocalDate endDate,
        String imageUrl
) {
    public Challenge toEntity(Cafe cafe) {
        return Challenge.builder()
                .name(name)
                .cafe(cafe)
                .type(type)
                .reward(reward)
                .startDate(startDate)
                .endDate(endDate)
                .imageUrl(imageUrl)
                .successCount(10)
                .build();
    }
}
