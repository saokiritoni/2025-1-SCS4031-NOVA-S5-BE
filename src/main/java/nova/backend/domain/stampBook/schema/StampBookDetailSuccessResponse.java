package nova.backend.domain.stampBook.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import nova.backend.domain.cafe.dto.response.CafeDesignOverviewDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.global.common.SuccessResponse;

@Schema(name = "StampBookDetailSuccessResponse", description = "스탬프북 단일 상세 조회 응답")
public class StampBookDetailSuccessResponse extends SuccessResponse<StampBookDetailSuccessResponse.StampBookDetailBody> {

    @Schema(name = "StampBookDetailBody", description = "스탬프북 상세 조회 응답 데이터")
    public static class StampBookDetailBody {

        @Schema(description = "카페 디자인 및 기본 정보")
        public CafeDesignOverviewDTO cafeDesignOverview;

        @Schema(description = "스탬프북 정보 (진행 상태, 개수 등)")
        public StampBookResponseDTO stampBookInfo;

        @Schema(description = "해당 카페에서 리워드 전환 가능한 스탬프북 개수")
        public int rewardAvailableCount;
    }
}
