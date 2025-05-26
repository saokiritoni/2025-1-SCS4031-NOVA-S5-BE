package nova.backend.domain.challenge.dto.response;

import nova.backend.domain.challenge.dto.common.ChallengeBaseDTO;

/**
 * 진행예정 / 진행중 챌린지 응답에 사용하는 DTO
 * @param base
 */
public record ChallengeSummaryDTO(
        ChallengeBaseDTO base
) {}


