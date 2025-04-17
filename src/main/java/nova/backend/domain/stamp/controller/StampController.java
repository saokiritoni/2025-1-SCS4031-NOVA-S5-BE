package nova.backend.domain.stamp.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stamp.dto.request.StampAccumulateRequestDTO;
import nova.backend.domain.stamp.dto.response.StampHistoryResponseDTO;
import nova.backend.domain.stamp.service.StampService;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stamps")
public class StampController implements StampApi {

    private final StampService stampService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> accumulateStamp(@AuthenticationPrincipal Long userId,
                                                              @RequestBody StampAccumulateRequestDTO request) {
        stampService.accumulateStamp(userId, request.cafeId(), request.count());
        return SuccessResponse.ok(null);
    }

    @GetMapping("/history")
    public ResponseEntity<SuccessResponse<?>> getStampHistory(@AuthenticationPrincipal Long userId) {
        List<StampHistoryResponseDTO> history = stampService.getStampHistory(userId);
        return SuccessResponse.ok(history);
    }
}
