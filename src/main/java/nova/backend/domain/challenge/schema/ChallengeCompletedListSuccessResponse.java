package nova.backend.domain.challenge.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.challenge.dto.response.OwnerCompletedChallengeListResponseDTO;
import nova.backend.global.common.SuccessResponse;

import java.util.List;

@Schema(description = "완료된 챌린지 목록 조회 성공 응답")
public class ChallengeCompletedListSuccessResponse extends SuccessResponse<List<OwnerCompletedChallengeListResponseDTO>> {
}

