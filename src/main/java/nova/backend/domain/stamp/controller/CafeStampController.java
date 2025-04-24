package nova.backend.domain.stamp.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stamp.dto.request.StampAccumulateRequestDTO;
import nova.backend.domain.stamp.service.StampService;
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
@RequestMapping("/api/cafe/stamps")
public class CafeStampController implements CafeStampApi {

    private final StampService stampService;

    // QR 코드로 유저의 스탬프 적립
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> accumulateStamp(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampAccumulateRequestDTO request) {

        stampService.accumulateStamp(userDetails.getUser(), request.targetQrCode(), request.cafeId(), request.count());
        return SuccessResponse.ok(null);
    }


}
