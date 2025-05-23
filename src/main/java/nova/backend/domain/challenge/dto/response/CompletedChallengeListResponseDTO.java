package nova.backend.domain.challenge.dto.response;

/**
 * 완료된 챌린지 리스트에 사용하는 DTO.
 * @param base
 * @param participantCount
 */
public record CompletedChallengeListResponseDTO(
        ChallengeBaseDTO base,
        int participantCount
) {}


