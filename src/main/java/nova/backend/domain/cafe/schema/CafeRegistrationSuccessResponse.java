package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nova.backend.global.common.SuccessResponse;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "카페 등록 성공 응답")
public class CafeRegistrationSuccessResponse extends SuccessResponse<Long> {
}
