package nova.backend.domain.challenge.dto.response;

import nova.backend.domain.challenge.dto.common.ChallengeBaseDTO;
import nova.backend.domain.challenge.entity.Challenge;

public record OwnerChallengeDetailResponseDTO(
        ChallengeBaseDTO base,
        int inProgressCount,
        int canceledCount,
        int rewardedCount
) {
    public static OwnerChallengeDetailResponseDTO fromEntity(Challenge challenge) {
        return new OwnerChallengeDetailResponseDTO(
                ChallengeBaseDTO.fromEntity(challenge),
                challenge.getInProgressCount(),
                challenge.getCanceledCount(),
                challenge.getRewardedCount()
        );
    }
}
