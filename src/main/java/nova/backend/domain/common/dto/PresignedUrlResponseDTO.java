package nova.backend.domain.common.dto;

public record PresignedUrlResponseDTO(
        String presignedUrl
) {
    public static PresignedUrlResponseDTO of(String presignedUrl) {
        return new PresignedUrlResponseDTO(presignedUrl);
    }
}
