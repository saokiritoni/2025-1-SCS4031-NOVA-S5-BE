package nova.backend.domain.stampBook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stampBook.dto.request.UseRewardsRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "카페(OWNER/STAFF)용 스탬프북 API", description = "사장/직원용 리워드 사용 API")
public interface StaffStampBookApi {

    @Operation(
            summary = "QR 코드로 리워드 사용 처리",
            description = "카페 사장/직원이 대상 유저의 QR 코드(UUID)를 사용하여 리워드를 사용 처리합니다.\n" +
                    "요청 수량보다 보유 리워드가 적으면 400 예외 발생.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리워드 사용 처리 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "사용 가능한 리워드 부족 (NOT_ENOUGH_REWARDS)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "대상 사용자 또는 카페를 찾을 수 없음, cafeSelection을 했는지 먼저 확인해주세요.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/rewards/use-by-qr")
    ResponseEntity<SuccessResponse<?>> useRewardsByQrCodeForCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UseRewardsRequestDTO request
    );
}
