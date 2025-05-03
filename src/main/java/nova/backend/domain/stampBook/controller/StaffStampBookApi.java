package nova.backend.domain.stampBook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stampBook.dto.request.UseRewardsRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "카페(OWNER/STAFF)용 스탬프북 API", description = "사장/직원용 리워드 사용 API")
public interface StaffStampBookApi {

    @Operation(
            summary = "QR 코드로 리워드 사용 처리",
            description = "카페 사장/직원이 대상 유저의 Profile QR 코드(UUID)를 사용하여, 사용 가능한 리워드를 지정된 개수를 count에 담아 사용 처리합니다.\n" +
                    "사용 가능한 리워드 개수를 초과했을 경우 예외를 던집니다.\n"+
                    "주의할 점은 카페 아이디가 필요합니다. 사장이 여러 개의 카페를 가질 수 있어서, 처음에는 jwt에서 카페 정보를 가져왔지만 그렇게 할 수 없어졌습니다! 참고바랍니다.\n" +
                    "이 부분에 대한 처리를 어떻게 할 지 약간의 논의와 고민이 필요합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @RequestBody(
            description = "QR 코드 값, 카페 ID, 사용 요청 리워드 개수를 포함한 요청 DTO",
            required = true,
            content = @Content(schema = @Schema(implementation = UseRewardsRequestDTO.class))
    )
    @ApiResponse(
            responseCode = "200",
            description = "리워드 사용 처리 성공",
            content = @Content(schema = @Schema(implementation = SuccessResponse.class))
    )
    @PatchMapping("/rewards/use-by-qr")
    ResponseEntity<SuccessResponse<?>> useRewardsByQrCodeForCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody UseRewardsRequestDTO request
    );

}
