package nova.backend.domain.user.dto.response;

public record QrCodeResponseDTO(
        String qrCodeValue,
        String name
) {
    public static QrCodeResponseDTO from(String qrCodeValue, String name) {
        return new QrCodeResponseDTO(qrCodeValue, name);
    }
}
