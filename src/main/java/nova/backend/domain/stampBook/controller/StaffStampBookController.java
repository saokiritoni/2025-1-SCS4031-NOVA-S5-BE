package nova.backend.domain.stampBook.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stampBook.controller.api.StaffStampBookApi;
import nova.backend.domain.stampBook.dto.request.UseRewardsRequestDTO;
import nova.backend.domain.stampBook.service.StaffStampBookService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/stampbooks")
public class StaffStampBookController implements StaffStampBookApi {

    private final StaffStampBookService staffStampBookService;

    @PatchMapping("/rewards")
    public ResponseEntity<SuccessResponse<?>> useRewardsByQrCodeForCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UseRewardsRequestDTO request
    ) {
        int updatedCount = staffStampBookService.useRewardsByQrCodeForCafe(
                userDetails.getUserId(), userDetails.getSelectedCafeId(), request.qrCodeValue(), request.count()
        );
        return SuccessResponse.ok("사용 처리된 리워드 수: " + updatedCount);
    }

}
