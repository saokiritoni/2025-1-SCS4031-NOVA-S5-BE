package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.controller.api.StaffCafeApi;
import nova.backend.domain.cafe.dto.response.CafeMyListItemDTO;
import nova.backend.domain.cafe.dto.response.CafeOwnerSelectedResponseDTO;
import nova.backend.domain.cafe.service.StaffCafeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/cafes")
public class StaffCafeController implements StaffCafeApi {
    private final StaffCafeService staffCafeService;

    @PutMapping("/{cafeId}/selected")
    public ResponseEntity<SuccessResponse<?>> selectCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cafeId
    ) {
        staffCafeService.selectCafe(userDetails.getUserId(), cafeId);
        return SuccessResponse.ok("선택된 카페 설정 완료");
    }

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyCafes(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CafeMyListItemDTO> response = staffCafeService.getMyCafes(
                userDetails.getUserId(),
                userDetails.getSelectedCafeId()
        );
        return SuccessResponse.ok(response);
    }

    @GetMapping("/selected")
    public ResponseEntity<SuccessResponse<?>> getSelectedCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CafeOwnerSelectedResponseDTO response = staffCafeService.getSelectedCafe(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(response);
    }
}
