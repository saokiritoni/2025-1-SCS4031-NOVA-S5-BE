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
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. 유저(USER) 스탬프북", description = "스탬프북 관련 API, 스탬프는 단일 스탬프지만 스탬프북은 한 장을 관리합니다.")
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

    @Operation(
            summary = "스탬프북 리워드 전환",
            description = "스탬프북을 리워드로 전환합니다.\n전환 조건이 충족되지 않으면 예외 발생.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리워드 전환 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 미완료 스탬프북)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "스탬프북을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{stampBookId}/reward")
    ResponseEntity<SuccessResponse<?>> convertToReward(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    );

    @Operation(
            summary = "스탬프북을 메인페이지에 추가",
            description = "선택한 스탬프북을 메인페이지에 표시되도록 추가합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메인페이지 추가 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "스탬프북을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{stampBookId}/home")
    ResponseEntity<SuccessResponse<?>> addStampBookToHome(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    );

    @Operation(
            summary = "스탬프북을 메인페이지에서 제거",
            description = "선택한 스탬프북을 메인페이지에서 제거합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메인페이지 제거 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "404", description = "스탬프북을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{stampBookId}/home")
    ResponseEntity<SuccessResponse<?>> removeStampBookFromHome(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    );

    @Operation(
            summary = "메인페이지용 스탬프북 목록 조회 (inHome=true)",
            description = "메인페이지에 표시되는 스탬프북 목록만 조회합니다.",
            security = @SecurityRequirement(name = "token")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메인페이지 스탬프북 조회 성공",
                    content = @Content(schema = @Schema(implementation = StampBookListSuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my/home")
    ResponseEntity<SuccessResponse<?>> getMyHomeStampBooks(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );
}
