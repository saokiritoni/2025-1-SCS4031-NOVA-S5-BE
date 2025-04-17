package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.global.common.SuccessResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "카페 목록 조회 성공 응답")
public class CafeListSuccessResponse extends SuccessResponse<List<CafeListResponseDTO>> {
}
