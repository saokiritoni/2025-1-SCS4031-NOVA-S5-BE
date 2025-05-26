package nova.backend.domain.challenge.dto.common;

import nova.backend.domain.challenge.entity.Challenge;
import nova.backend.domain.challenge.entity.ChallengeType;

import java.time.LocalDate;

/**
 * 챌린지 공통 응답 DTO.
 * @param challengeId
 * @param type
 * @param rewardDescription
 * @param startDate
 * @param endDate
 * @param thumbnailUrl
 */
public record ChallengeBaseDTO(
        Long challengeId,
        ChallengeType type,
        String rewardDescription,
        LocalDate startDate,
        LocalDate endDate,
        String thumbnailUrl
) {
    public static ChallengeBaseDTO fromEntity(Challenge challenge) {
        return new ChallengeBaseDTO(
                challenge.getChallengeId(),
                challenge.getType(),
                challenge.getReward(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getImageUrl()
        );
    }
}

