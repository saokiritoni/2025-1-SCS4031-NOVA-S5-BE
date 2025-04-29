package nova.backend.domain.stampBook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.stampBook.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.schema.StampBookListSuccessResponse;
import nova.backend.domain.stampBook.schema.StampBookSuccessResponse;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "스탬프북 API", description = "스탬프북 관련 API, 스탬프는 단일 스탬프지만 스탬프북은 한 장을 관리합니다.")
public interface StampBookApi {

    @Operation(summary = "나의 스탬프북 목록 조회 (for 메인페이지 / 스탬프북 페이지)",
            description = "나의 스탬프북 목록을 모두 조회합니다. 현재는 리워드 전환 여부 관계없이 모두 표시하고 있습니다. 이후에는 리워드 전환된 스탬프북은 표시하지 않을 예정입니다. (구현 예정)" +
                    "\n inHome=true이면 메인페이지에서 표시해야 합니다.",
            security = @SecurityRequirement(name = "token"))
    @ApiResponse(responseCode = "200", description = "스탬프북 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StampBookListSuccessResponse.class)
            )
    )
    @GetMapping("/my")
    ResponseEntity<SuccessResponse<?>> getMyStampBooks(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "스탬프북 다운로드(생성)",
            description = "카페 목록에서 스탬프북 다운로드를 하면 이 API를 사용해야 합니다. 찍힌 스탬프 개수가 0인 새로운 스탬프북이 생성됩니다. 채우지 않은 스탬프북이 이미 있는데 또 다운로드 받는 경우 에러를 던집니다.",
            security = @SecurityRequirement(name = "token"))
    @ApiResponse(responseCode = "201", description = "스탬프북 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StampBookSuccessResponse.class)
            )
    )
    @PostMapping
    ResponseEntity<SuccessResponse<?>> createStampBook(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookCreateRequestDTO request
    );
}
