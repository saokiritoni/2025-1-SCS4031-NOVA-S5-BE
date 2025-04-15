package nova.backend.global.util;

import java.util.Base64;
import java.util.UUID;

public class QrCodeGenerator {
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.toString().getBytes());
    }
}
