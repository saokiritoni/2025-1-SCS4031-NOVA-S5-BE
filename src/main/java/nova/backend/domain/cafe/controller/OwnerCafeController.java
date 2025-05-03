package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.service.CafeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class OwnerCafeController implements OwnerCafeApi{

    private final CafeService cafeService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<?>> registerCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CafeRegistrationRequestDTO request
    ) {
        Long ownerId = userDetails.getUserId();
        Cafe savedCafe = cafeService.registerCafe(ownerId, request);
        return SuccessResponse.ok(savedCafe.getCafeId());
    }

}
