package nova.backend.domain.challenge.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
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
 * @param cafeId
 * @param cafeName
 */
public record ChallengeBaseDTO(
        Long challengeId,
        ChallengeType type,
        String rewardDescription,
        LocalDate startDate,
        LocalDate endDate,
        String thumbnailUrl,
        Long cafeId,
        String cafeName
) {
    public static ChallengeBaseDTO fromEntity(Challenge challenge) {
        Cafe cafe = challenge.getCafe();
        return new ChallengeBaseDTO(
                challenge.getChallengeId(),
                challenge.getType(),
                challenge.getReward(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getImageUrl(),
                cafe != null ? cafe.getCafeId() : null,
                cafe != null ? cafe.getCafeName() : null
        );
    }
}

