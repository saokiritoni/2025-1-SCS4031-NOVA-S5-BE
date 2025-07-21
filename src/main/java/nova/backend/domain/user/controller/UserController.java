package nova.backend.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.domain.user.dto.response.UserStatusResponseDTO;
import nova.backend.domain.user.service.UserService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @GetMapping("/my-qr")
    public ResponseEntity<SuccessResponse<?>> getMyQrCode(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = userDetails.getUserId();
        QrCodeResponseDTO qrCode = userService.getMyQrCode(userId);
        return SuccessResponse.ok(qrCode);
    }

    @GetMapping("/status")
    public ResponseEntity<SuccessResponse<?>> getUserStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserStatusResponseDTO response = userService.getUserStatus(userDetails.getUserId());
        return SuccessResponse.ok(response);
    }

}
