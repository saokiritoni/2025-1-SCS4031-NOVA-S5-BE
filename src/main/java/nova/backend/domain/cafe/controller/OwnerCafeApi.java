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
import nova.backend.domain.cafe.schema.CafeRegistrationMultipartSchema;
import nova.backend.domain.stampBook.dto.request.StampBookDesignUpdateRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "4. 사장(OWNER) API", description = "카페 등록/관리 API")
public interface OwnerCafeApi {

    @Operation(
            summary = "카페 등록",
            description = "사장이 자신의 카페를 등록합니다. 요청은 multipart/form-data 형식이며, PDF 파일을 포함합니다.",
            security = @SecurityRequirement(name = "token"),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(implementation = CafeRegistrationMultipartSchema.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카페 등록 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 등록된 카페",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/register", consumes = "multipart/form-data")
    ResponseEntity<SuccessResponse<?>> registerCafe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute CafeRegistrationRequestDTO request,
            @RequestPart MultipartFile businessRegistrationPdf
    );

    @Operation(
            summary = "스탬프북 커스텀 디자인 저장",
            description = "선택된 카페에 대해 스탬프북 디자인을 저장합니다.\ncafeId는 JWT에서 추출된 selectedCafeId를 사용합니다.",
            security = @SecurityRequirement(name = "token"),
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StampBookDesignUpdateRequestDTO.class)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "디자인 저장 성공",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/stampbook-design")
    ResponseEntity<SuccessResponse<?>> updateStampBookDesign(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.RequestBody StampBookDesignUpdateRequestDTO request
    );
}