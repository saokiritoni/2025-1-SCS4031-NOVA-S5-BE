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

    @Operation(summary = "카페 목록 조회", description = "모든 카페의 정보를 조회합니다.")
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
