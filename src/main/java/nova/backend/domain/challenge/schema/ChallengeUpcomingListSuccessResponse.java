package nova.backend.domain.challenge.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.challenge.dto.response.ChallengeSummaryDTO;
import nova.backend.global.common.SuccessResponse;

import java.util.List;

@Schema(description = "예정 챌린지 목록 조회 성공 응답")
public class ChallengeUpcomingListSuccessResponse extends SuccessResponse<List<ChallengeSummaryDTO>> {
}


