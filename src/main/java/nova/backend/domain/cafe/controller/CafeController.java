package nova.backend.domain.cafe.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.service.CafeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class CafeController implements CafeApi {

    private final CafeService cafeService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getCafeList(
            @RequestParam(required = false) Boolean approved
    ) {
        List<CafeListResponseDTO> response;
        if (approved != null && approved) {
            response = cafeService.getApprovedCafes();
        } else {
            response = cafeService.getAllCafes();
        }
        return SuccessResponse.ok(response);
    }

}


