package nova.backend.domain.stamp.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nova.backend.domain.stamp.dto.response.StampHistoryResponseDTO;
import nova.backend.global.common.SuccessResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "스탬프 적립/사용 히스토리 목록 응답")
public class StampHistoryListSuccessResponse extends SuccessResponse<List<StampHistoryResponseDTO>> {
}
