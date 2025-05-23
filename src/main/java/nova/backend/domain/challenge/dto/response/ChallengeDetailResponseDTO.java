package nova.backend.domain.challenge.dto.response;

import nova.backend.domain.challenge.entity.Challenge;

public record ChallengeDetailResponseDTO(
        ChallengeBaseDTO base,
        int participantCount,
        int completedCount,
        int canceledCount
) {
    public static ChallengeDetailResponseDTO fromEntity(Challenge challenge) {
        return new ChallengeDetailResponseDTO(
                ChallengeBaseDTO.fromEntity(challenge),
                challenge.getParticipantCount(),
                challenge.getCompletedCount(),
                challenge.getCanceledCount()
        );
    }
}
