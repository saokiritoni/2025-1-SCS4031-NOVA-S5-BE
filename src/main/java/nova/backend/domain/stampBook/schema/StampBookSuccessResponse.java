package nova.backend.domain.stampBook.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.global.common.SuccessResponse;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "스탬프북 생성 성공 응답")
public class StampBookSuccessResponse extends SuccessResponse<StampBookResponseDTO> {
}
