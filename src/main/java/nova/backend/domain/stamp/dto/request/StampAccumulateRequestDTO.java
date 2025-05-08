package nova.backend.domain.stamp.dto.request;

public record StampAccumulateRequestDTO(
        String qrCodeValue,
        int count
) {}


