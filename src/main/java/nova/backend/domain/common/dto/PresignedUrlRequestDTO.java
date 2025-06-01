package nova.backend.domain.common.dto;

public record PresignedUrlRequestDTO(
        String directory,
        String fileName
) {
}
