package nova.backend.domain.stamp.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stamp.dto.request.StampAccumulateRequestDTO;
import nova.backend.domain.stamp.dto.response.StaffStampViewResponseDTO;
import nova.backend.domain.stamp.service.StampService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/stamps")
public class StaffStampController implements StaffStampApi {

    private final StampService stampService;

    // QR 코드로 유저의 스탬프 적립
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> accumulateStamp(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampAccumulateRequestDTO request) {

        stampService.accumulateStamp(userDetails, request.qrCodeValue(), request.count());
        return SuccessResponse.ok(null);
    }

    // 적립 후 유저 정보 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getStampHistoryForStaffView(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String qrCodeValue) {
        StaffStampViewResponseDTO result = stampService.getStampHistoryForStaffView(qrCodeValue, userDetails);
        return SuccessResponse.ok(result);
    }


}
