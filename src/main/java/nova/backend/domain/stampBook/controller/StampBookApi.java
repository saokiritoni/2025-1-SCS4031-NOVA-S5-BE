package nova.backend.domain.stampBook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stampBook.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.schema.StampBookListSuccessResponse;
import nova.backend.domain.stampBook.schema.StampBookSuccessResponse;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "스탬프북 API", description = "스탬프북 관련 API, 스탬프는 단일 스탬프지만 스탬프북은 한 장을 관리합니다.")
public interface StampBookApi {

    @Operation(
            summary = "나의 스탬프북 목록 조회 (for 메인페이지 / 스탬프북 페이지)",
            description = "나의 스탬프북 목록을 모두 조회합니다.\n리워드 전환 여부 관계없이 모두 표시하고 있습니다.\n(inHome=true이면 메인페이지용)",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스탬프북 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = StampBookListSuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my")
    ResponseEntity<SuccessResponse<?>> getMyStampBooks(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(
            summary = "스탬프북 다운로드(생성)",
            description = "카페 목록에서 스탬프북 다운로드 시 사용.\n이미 생성된 미완료 스탬프북이 있으면 409 예외 발생.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "스탬프북 생성 성공",
                    content = @Content(schema = @Schema(implementation = StampBookSuccessResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 생성된 스탬프북 존재 (STAMPBOOK_ALREADY_EXISTS)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    ResponseEntity<SuccessResponse<?>> createStampBook(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookCreateRequestDTO request
    );
}

