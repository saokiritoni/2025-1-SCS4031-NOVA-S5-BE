package nova.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.domain.user.service.UserService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/my-qr")
    public ResponseEntity<SuccessResponse<?>> getMyQrCode(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();
        QrCodeResponseDTO qrCode = userService.getMyQrCode(userId);
        return SuccessResponse.ok(qrCode);
    }
}
