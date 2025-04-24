package nova.backend.domain.stamp.dto.request;

public record StampAccumulateRequestDTO(
        Long cafeId,
        int count,
        String targetQrCode
) {}

