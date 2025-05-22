package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.request.StampBookDesignCreateRequestDTO;
import nova.backend.domain.cafe.dto.response.CafeDetailResponseDTO;
import nova.backend.domain.cafe.dto.response.StampBookDesignResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.StampBookDesign;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.service.OwnerCafeService;
import nova.backend.domain.cafe.dto.request.StampBookDesignUpdateRequestDTO;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static nova.backend.global.error.ErrorCode.ENTITY_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/cafes")
public class OwnerCafeController implements OwnerCafeApi {

    private final OwnerCafeService ownerCafeService;
    private final CafeRepository cafeRepository;

    // 가게 등록
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

    // 스탬프북 디자인 추가
    @PostMapping("/stampbook-design")
    public ResponseEntity<SuccessResponse<?>> addStampBookDesign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookDesignCreateRequestDTO request
    ) {
        Long cafeId = userDetails.getSelectedCafeId();
        ownerCafeService.addStampBookDesign(userDetails.getUserId(), cafeId, request);
        return SuccessResponse.ok("디자인이 추가되었습니다.");
    }

    // 스탬프북 디자인 노출 설정
    @PatchMapping("/stampbook-design/{designId}/expose")
    public ResponseEntity<SuccessResponse<?>> exposeStampBookDesign(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long designId
    ) {
        ownerCafeService.setExposedStampBookDesign(userDetails.getUserId(), userDetails.getSelectedCafeId(), designId);
        return SuccessResponse.ok("해당 디자인이 노출 디자인으로 설정되었습니다.");
    }

    // 스탬프북 디자인만 반환
    @GetMapping("/stampbook-designs")
    public ResponseEntity<SuccessResponse<?>> getAllStampBookDesigns(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<StampBookDesignResponseDTO> designs = ownerCafeService
                .getAllStampBookDesigns(userDetails.getUserId(), userDetails.getSelectedCafeId())
                .stream()
                .map(StampBookDesignResponseDTO::fromEntity)
                .toList();

        return SuccessResponse.ok(designs);
    }


    // 노출된 스탬프북 디자인만 반환
    @GetMapping("/stampbook-design")
    public ResponseEntity<SuccessResponse<?>> getExposedStampBookDesign(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        StampBookDesign exposed = ownerCafeService.getExposedStampBookDesign(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(exposed);
    }

    @GetMapping("/detail")
    public ResponseEntity<SuccessResponse<?>> getCafeDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Cafe cafe = cafeRepository.findById(userDetails.getSelectedCafeId())
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
        return SuccessResponse.ok(CafeDetailResponseDTO.fromEntity(cafe));
    }

    @GetMapping("/stampbook-design/{designId}")
    public ResponseEntity<SuccessResponse<?>> getStampBookDesignById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long designId
    ) {
        StampBookDesign design = ownerCafeService.getStampBookDesignById(userDetails.getUserId(), userDetails.getSelectedCafeId(), designId);
        return SuccessResponse.ok(StampBookDesignResponseDTO.fromEntity(design));
    }


}