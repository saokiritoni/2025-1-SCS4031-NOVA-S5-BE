package nova.backend.domain.user.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.user.dto.response.UserStatusResponseDTO;
import nova.backend.global.common.SuccessResponse;

@Schema(description = "사용자 상태 정보 조회 성공 응답")
public class UserStatusSuccessResponse extends SuccessResponse<UserStatusResponseDTO> {}
