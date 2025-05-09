package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "카페 선택 API", description = "사장/직원이 현재 선택된 카페를 설정하는 API")
public interface CafeSelectionApi {

    @Operation(
            summary = "현재 카페 선택",
            description = "사장 또는 직원이 로그인 후, 소속된 카페 중 하나를 선택하여 '현재 카페'로 설정합니다. " +
                    "cafeId가 필요한 곳에(ex. 스탬프 적립 등) 지정하지 않고 JWT에 담아서 자동 추출합니다. " +
                    "현재 카페가 설정되지 않은 상태로 작업할 경우 에러를 반환합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카페 선택 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (예: 토큰 문제)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족 (해당 카페에 접근 권한 없음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{cafeId}/select")
    ResponseEntity<SuccessResponse<?>> selectCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "선택할 카페의 ID", example = "1") @PathVariable Long cafeId
    );
}


