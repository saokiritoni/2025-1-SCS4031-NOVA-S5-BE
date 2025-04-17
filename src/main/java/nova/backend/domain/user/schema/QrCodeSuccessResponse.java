package nova.backend.domain.user.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.global.common.SuccessResponse;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "QR 코드 조회 성공 응답")
public class QrCodeSuccessResponse extends SuccessResponse<QrCodeResponseDTO> {
}
