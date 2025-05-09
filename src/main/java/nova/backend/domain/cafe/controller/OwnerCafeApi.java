package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "4. 사장(OWNER) API", description = "카페 등록/관리 API")
public interface OwnerCafeApi {

    @Operation(
            summary = "카페 등록",
            description = "사장이 자신의 카페를 등록합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @RequestBody(
            description = "카페 이름, 지점명, 사장 이름, 전화번호, 사업자 등록번호, 위치 좌표, 최대 스탬프 수, 캐릭터 타입, 리워드 설명을 포함한 요청 DTO",
            required = true,
            content = @Content(schema = @Schema(implementation = CafeRegistrationRequestDTO.class))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카페 등록 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (예: 토큰 문제)",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족 (Owner 권한 아님)",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 등록된 카페",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class)))
    })
    @PostMapping("/register")
    ResponseEntity<SuccessResponse<?>> registerCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody CafeRegistrationRequestDTO request
    );
}
