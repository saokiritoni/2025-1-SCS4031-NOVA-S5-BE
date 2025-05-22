package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.response.CafeDetailResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.service.OwnerCafeService;
import nova.backend.domain.stampBook.dto.request.StampBookDesignUpdateRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static nova.backend.global.error.ErrorCode.ENTITY_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/cafes")
public class OwnerCafeController implements OwnerCafeApi {

    private final OwnerCafeService ownerCafeService;
    private final CafeRepository cafeRepository;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<?>> registerCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute CafeRegistrationRequestDTO request,
            @RequestPart MultipartFile businessRegistrationPdf
    ) {
        Long ownerId = userDetails.getUserId();
        Cafe savedCafe = ownerCafeService.registerCafe(ownerId, request, businessRegistrationPdf);
        return SuccessResponse.ok(savedCafe.getCafeId());
    }

    @PatchMapping("/stampbook-design")
    public ResponseEntity<SuccessResponse<?>> updateStampBookDesign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookDesignUpdateRequestDTO request
    ) {
        Long selectedCafeId = userDetails.getSelectedCafeId();
        ownerCafeService.updateStampBookDesign(userDetails.getUserId(), selectedCafeId, request.designJson());
        return SuccessResponse.ok("스탬프북 커스텀이 저장되었습니다.");
    }

    @GetMapping("/stampbook-design")
    public ResponseEntity<SuccessResponse<?>> getStampBookDesign(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Cafe cafe = cafeRepository.findById(userDetails.getSelectedCafeId())
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
        return SuccessResponse.ok(cafe.getStampBookDesignJson());
    }

    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<?>> getCafeDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Cafe cafe = cafeRepository.findById(userDetails.getSelectedCafeId())
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
        return SuccessResponse.ok(CafeDetailResponseDTO.fromEntity(cafe));
    }

}
