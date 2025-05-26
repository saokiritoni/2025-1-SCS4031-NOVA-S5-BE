package nova.backend.domain.challenge.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.cafe.dto.response.CafeDetailResponseDTO;
import nova.backend.global.common.SuccessResponse;

import java.util.List;

@Schema(description = "카페 목록 조회 성공 응답")
public class CafeListSuccessResponse extends SuccessResponse<List<CafeDetailResponseDTO>> {}

