package nova.backend.domain.stampBook.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stampBook.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.service.StampBookService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stampbooks")
public class StampBookController implements StampBookApi{

    private final StampBookService stampBookService;

    // 유저의 모든 스탬프북 조회
    // TODO: 리워드 전환한 스탬프북 미조회 -> 리워드로만 넘기기
    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyStampBooks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();
        List<StampBookResponseDTO> response = stampBookService.getStampBooksForUser(userId);
        return SuccessResponse.ok(response);
    }

    // 스탬프북 다운로드 (생성하기)
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createStampBook(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookCreateRequestDTO request
    ) {
        Long userId = userDetails.getUserId();
        StampBookResponseDTO response = stampBookService.createStampBook(userId, request.cafeId());
        return SuccessResponse.created(response);
    }

}
