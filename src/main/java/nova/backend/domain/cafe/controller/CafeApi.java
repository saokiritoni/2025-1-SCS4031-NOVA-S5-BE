package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.cafe.schema.CafeListSuccessResponse;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "카페 API", description = "카페 목록 관련 API")
public interface CafeApi {

    @Operation(summary = "카페 목록 조회", description = "모든 카페의 정보를 조회합니다. isOpen의 종류가 2개 있습니다. " +
            "\n 1. 전체 isOpen: 카페 운영시간(일반 운영시간, 임시 운영시간) & 현재 날짜/시간에 따라 전체 isOpen으로 현재 오픈 여부를 판단합니다. " +
            "\n 2. 요일별 isOpen: 카페에서 설정한 요일별 오픈 여부입니다." +
            "\n * Special Days: 카페에서 지정한 임시 휴일/공휴일입니다. specialDays의 isOpen값이 일반 운영시간보다 우선순위를 가집니다." +
            "\n * Special Days를 월별로만 가져오는 추가 처리가 필요할 것 같습니다. 현재는 전체 specialDay를 가져옵니다.")
    @ApiResponse(
            responseCode = "200",
            description = "카페 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CafeListSuccessResponse.class)
            )
    )
    @GetMapping
    ResponseEntity<SuccessResponse<?>> getCafeList();
}
