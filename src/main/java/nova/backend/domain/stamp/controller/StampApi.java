package nova.backend.domain.stamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stamp.schema.StampHistoryListSuccessResponse;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "스탬프 API", description = "스탬프 적립 및 조회 API")
public interface StampApi {

    @Operation(
            summary = "스탬프 적립/사용 내역 조회",
            description = "내가 적립하거나 사용한 스탬프의 히스토리를 모두 조회합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "스탬프 내역 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StampHistoryListSuccessResponse.class)
            )
    )
    @GetMapping("/history")
    ResponseEntity<SuccessResponse<?>> getStampHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId
    );
}
