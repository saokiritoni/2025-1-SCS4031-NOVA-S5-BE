package nova.backend.domain.challenge.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.global.common.SuccessResponse;

@Schema(description = "챌린지 적립 성공 응답")
public class ChallengeAccumulateSuccessResponse extends SuccessResponse<Void> {
}
