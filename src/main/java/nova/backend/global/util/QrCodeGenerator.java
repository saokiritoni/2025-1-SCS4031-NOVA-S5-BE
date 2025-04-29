package nova.backend.global.util;

import java.util.Base64;
import java.util.UUID;

/**
 유저의 UUID 반환 -> 스탬프 적립, 챌린지 인증에 사용하는 클래스
 **/
public class QrCodeGenerator {
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.toString().getBytes());
    }
}
