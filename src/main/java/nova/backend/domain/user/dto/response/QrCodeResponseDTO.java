package nova.backend.domain.user.dto.response;

public record QrCodeResponseDTO(
        String qrCodeValue
) {
    public static QrCodeResponseDTO from(String qrCodeValue) {
        return new QrCodeResponseDTO(qrCodeValue);
    }
}
