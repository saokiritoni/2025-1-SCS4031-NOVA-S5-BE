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
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "카페(OWNER/STAFF)용 스탬프 API", description = "사장/직원용 스탬프 적립 및 조회 API")
public interface CafeStampApi {

    @Operation(
            summary = "스탬프 적립",
            description = "카페 사장/직원이 Profile QR 코드를 기반(UUID) 으로 유저에게 스탬프를 적립합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @RequestBody(
            description = "적립 대상 회원의 QR 코드, 카페 ID, 적립 개수를 포함한 요청 DTO",
            required = true,
            content = @Content(schema = @Schema(implementation = StampAccumulateRequestDTO.class))
    )
    @ApiResponse(
            responseCode = "200",
            description = "스탬프 적립 성공",
            content = @Content(schema = @Schema(implementation = SuccessResponse.class))
    )
    @PostMapping
    ResponseEntity<SuccessResponse<?>> accumulateStamp(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody StampAccumulateRequestDTO request
    );

}
