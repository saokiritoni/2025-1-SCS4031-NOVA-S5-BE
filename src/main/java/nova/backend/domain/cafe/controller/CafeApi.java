package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.cafe.schema.CafeListSuccessResponse;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "2. 유저(USER) 카페", description = "카페 목록 관련 API")
public interface CafeApi {

    @Operation(
            summary = "카페 목록 조회",
            description = "카페 목록을 조회합니다.\n\n" +
                    "✅ 쿼리 파라미터 `approved`가 true일 경우, **심사에 승인된 카페**만 반환됩니다. -> 일반 사용자용!\n" +
                    "✅ `approved`가 없거나 false일 경우, **전체 카페**가 반환됩니다. -> 관리자용!\n\n" +
                    "추가 설명:\n" +
                    "1. 전체 isOpen: 카페 운영시간(일반 운영시간, 임시 운영시간) & 현재 날짜/시간에 따라 전체 오픈 여부 판단\n" +
                    "2. 요일별 isOpen: 카페가 설정한 요일별 오픈 여부\n" +
                    "* Special Days: 카페 지정 임시 휴일/공휴일 (specialDays의 isOpen이 일반 운영시간보다 우선)\n" +
                    "* 현재 specialDays는 전체 조회 (월별 필터링은 추후 개선 예정)",
            parameters = {
                    @Parameter(name = "approved", description = "승인된 카페만 조회 여부 (true: 승인된 카페만, false/없음: 전체 카페)", example = "true")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카페 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CafeListSuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<SuccessResponse<?>> getCafeList(
            @RequestParam(required = false) Boolean approved
    );
}
