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
@Schema(description = "내가 소속된 카페 목록 응답")
public class MyCafeListSuccessResponse extends SuccessResponse<List<CafeListResponseDTO>> {
}
