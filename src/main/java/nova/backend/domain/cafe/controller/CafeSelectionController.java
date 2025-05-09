package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.service.CafeSelectionService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/cafes")
public class CafeSelectionController implements CafeSelectionApi{
    private final CafeSelectionService cafeSelectionService;

    @PutMapping("/{cafeId}/selected")
    public ResponseEntity<SuccessResponse<?>> selectCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cafeId
    ) {
        cafeSelectionService.selectCafe(userDetails.getUserId(), cafeId);
        return SuccessResponse.ok("선택된 카페 설정 완료");
    }

}

