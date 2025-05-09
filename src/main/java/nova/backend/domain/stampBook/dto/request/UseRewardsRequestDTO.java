package nova.backend.domain.stampBook.dto.request;

public record UseRewardsRequestDTO(
        String qrCodeValue,
        int count
) {}

