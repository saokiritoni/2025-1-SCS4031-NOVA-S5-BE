package nova.backend.domain.challenge.dto.response;

import nova.backend.domain.challenge.dto.common.ChallengeBaseDTO;

/**
 * 완료된 챌린지 리스트에 사용하는 DTO.
 * @param base
 * @param participantCount
 */
public record OwnerCompletedChallengeListResponseDTO(
        ChallengeBaseDTO base,
        int participantCount
) {}


