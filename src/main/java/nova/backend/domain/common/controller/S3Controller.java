package nova.backend.domain.common.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.common.dto.PresignedUrlRequestDTO;
import nova.backend.domain.common.dto.PresignedUrlResponseDTO;
import nova.backend.domain.common.service.S3Service;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/s3")
@RestController
public class S3Controller implements S3Api{

    private final S3Service s3Service;

    // S3 presigned URL 생성 API
    @PostMapping("presigned-url")
    public ResponseEntity<SuccessResponse<?>> generatePresignedUrl(@RequestBody PresignedUrlRequestDTO presignedUrlRequest) {
        PresignedUrlResponseDTO presignedUrl = s3Service.generatePresignedUrl(presignedUrlRequest);
        return SuccessResponse.ok(presignedUrl);
    }

    // 카페 사진 등록용 presigned URL 생성 API
    @PostMapping("presigned-url/cafe")
    public ResponseEntity<SuccessResponse<?>> generateCafePresignedUrl(@AuthenticationPrincipal Long userId) {
        PresignedUrlResponseDTO presignedUrl = s3Service.generateCafePresignedUrl(userId);
        return SuccessResponse.ok(presignedUrl);
    }

    // 프로필 사진 업로드용 presigned URL 생성 API
    @PostMapping("presigned-url/profile")
    public ResponseEntity<SuccessResponse<?>> generateProfilePresignedUrl(@AuthenticationPrincipal Long userId) {
        PresignedUrlResponseDTO presignedUrl = s3Service.generateProfilePresignedUrl(userId);
        return SuccessResponse.ok(presignedUrl);
    }
}
