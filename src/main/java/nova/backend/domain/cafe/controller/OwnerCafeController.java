package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.service.CafeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/cafes")
public class OwnerCafeController {

    private final CafeService cafeService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<?>> registerCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute CafeRegistrationRequestDTO request,
            @RequestPart MultipartFile businessRegistrationPdf
    ) {
        Long ownerId = userDetails.getUserId();
        Cafe savedCafe = cafeService.registerCafe(ownerId, request, businessRegistrationPdf);
        return SuccessResponse.ok(savedCafe.getCafeId());
    }

}
