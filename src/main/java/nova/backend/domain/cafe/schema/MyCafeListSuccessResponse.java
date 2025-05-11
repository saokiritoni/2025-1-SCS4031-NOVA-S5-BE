package nova.backend.domain.cafe.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;

import java.util.List;

@Schema(name = "MyCafeListSuccessResponse", description = "내가 소속된 카페 목록 반환 DTO")
public class MyCafeListSuccessResponse {

    @Schema(description = "응답 성공 여부", example = "true")
    public boolean success;

    @Schema(description = "응답 데이터")
    public List<MyCafe> data;

    public static class MyCafe {
        @Schema(description = "카페 ID", example = "1")
        public Long cafeId;

        @Schema(description = "카페 이름", example = "성균쓰커피")
        public String cafeName;

        @Schema(description = "카페 등록 상태", example = "APPROVED")
        public CafeRegistrationStatus registrationStatus;

        @Schema(description = "현재 선택된 카페 여부", example = "true")
        public boolean isSelected;
    }
}
