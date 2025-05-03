package nova.backend.domain.stampBook.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.service.CafeService;
import nova.backend.domain.stamp.service.StampService;
import nova.backend.domain.stampBook.dto.request.UseRewardsRequestDTO;
import nova.backend.domain.stampBook.service.StampBookService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/stampbooks")
public class CafeStampBookController {

    private final StampBookService stampBookService;

    @PatchMapping("/rewards/use-by-qr")
    public ResponseEntity<SuccessResponse<?>> useRewardsByQrCodeForCafe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UseRewardsRequestDTO request
    ) {
        int updatedCount = stampBookService.useRewardsByQrCodeForCafe(
                userDetails.getUserId(), request.qrCodeValue(), request.count()
        );
        return SuccessResponse.ok("사용 처리된 리워드 수: " + updatedCount);
    }

}
