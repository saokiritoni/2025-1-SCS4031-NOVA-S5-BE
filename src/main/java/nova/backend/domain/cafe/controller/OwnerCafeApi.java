package nova.backend.domain.cafe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.schema.CafeRegistrationMultipartSchema;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 등록된 카페",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러",
                    content = @Content(schema = @Schema(implementation = nova.backend.global.error.dto.ErrorResponse.class)))
    })
    @PostMapping(value = "/register", consumes = "multipart/form-data")
    ResponseEntity<SuccessResponse<?>> registerCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute CafeRegistrationRequestDTO request,
            @RequestPart MultipartFile businessRegistrationPdf
    );
}
