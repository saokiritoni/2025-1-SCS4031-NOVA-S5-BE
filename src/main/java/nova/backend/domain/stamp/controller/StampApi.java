package nova.backend.domain.stamp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stamp.dto.request.StampAccumulateRequestDTO;
import nova.backend.domain.stamp.schema.StampHistoryListSuccessResponse;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "스탬프 API", description = "스탬프 적립 및 조회 API")
public interface StampApi {

    @Operation(summary = "스탬프 적립",
            description = "카페 사장/직원이 QR 코드를 기반으로 스탬프를 적립합니다. 현재는 토큰을 사용하도록 되어있는데, qr-code-value를 사용해 적립하도록 수정할 예정입니다. (구현 예정)",
            security = @SecurityRequirement(name = "token"))
    @ApiResponse(responseCode = "200", description = "스탬프 적립 성공")
    @PostMapping
    ResponseEntity<SuccessResponse<?>> accumulateStamp(
            @Parameter(hidden = true) @AuthenticationPrincipal Long userId,
            @RequestBody(description = "카페 ID 및 적립 개수", required = true)
            StampAccumulateRequestDTO request
    );

    @Operation(summary = "스탬프 적립/사용 내역 조회", description = "내 스탬프 적립 및 사용 히스토리를 조회합니다.", security = @SecurityRequirement(name = "token"))
    @ApiResponse(responseCode = "200", description = "스탬프 내역 조회 성공",
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
