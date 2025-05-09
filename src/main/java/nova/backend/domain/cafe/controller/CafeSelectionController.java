package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.service.CafeSelectionService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class CafeSelectionController implements CafeSelectionApi{
    private final CafeSelectionService cafeSelectionService;

    @PostMapping("/{cafeId}/select")
    public ResponseEntity<SuccessResponse<?>> selectCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cafeId
    ) {
        cafeSelectionService.selectCafe(userDetails.getUserId(), cafeId);
        return SuccessResponse.ok("매장 선택 완료");
    }

}

