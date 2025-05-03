package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "카페(OWNER) API", description = "카페 등록 API")
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
    @ApiResponse(
            responseCode = "200",
            description = "카페 등록 성공",
            content = @Content(schema = @Schema(implementation = SuccessResponse.class))
    )
    @PostMapping("/register")
    ResponseEntity<SuccessResponse<?>> registerCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody CafeRegistrationRequestDTO request
    );

}
