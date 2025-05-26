package nova.backend.domain.challenge.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.challenge.dto.response.OwnerChallengeDetailResponseDTO;
import nova.backend.global.common.SuccessResponse;

@Schema(description = "챌린지 상세 조회 성공 응답")
public class ChallengeDetailSuccessResponse extends SuccessResponse<OwnerChallengeDetailResponseDTO> {
}
