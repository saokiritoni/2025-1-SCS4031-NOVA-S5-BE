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

    @PostMapping("/rewards/use")
    public ResponseEntity<SuccessResponse<?>> useMultipleRewards(
            @AuthenticationPrincipal CustomUserDetails staffDetails,
            @RequestBody UseRewardsRequestDTO request
    ) {
        int usedCount = stampBookService.useRewardsByQrCode(request.qrCodeValue(), request.count());
        return SuccessResponse.ok(usedCount + "개의 리워드를 사용 완료 처리했습니다.");
    }


}
