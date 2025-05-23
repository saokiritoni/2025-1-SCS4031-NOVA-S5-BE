package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeSummaryWithConceptDTO;
import nova.backend.domain.cafe.service.CafeService;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class CafeController implements CafeApi {

    private final CafeService cafeService;

    /**
     * 전체 카페 목록 또는 승인된 카페 목록 조회
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getCafeList(
            @RequestParam(required = false) Boolean approved
    ) {
        List<CafeSummaryWithConceptDTO> response;
        if (Boolean.TRUE.equals(approved)) {
            response = cafeService.getApprovedCafes();
        } else {
            response = cafeService.getAllCafes();
        }
        return SuccessResponse.ok(response);
    }

    /**
     * 다운로드 수 기준 인기 카페 TOP 10
     */
    @GetMapping("/popular")
    public ResponseEntity<SuccessResponse<?>> getPopularCafes() {
        List<CafeSummaryWithConceptDTO> response = cafeService.getTop10CafesByStampBookDownload();
        return SuccessResponse.ok(response);
    }
}
