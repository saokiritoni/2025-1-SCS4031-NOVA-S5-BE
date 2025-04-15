package nova.backend.domain.stampBook.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stampBook.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.service.StampBookService;
import nova.backend.domain.user.dto.response.QrCodeResponseDTO;
import nova.backend.domain.user.service.UserService;
import nova.backend.global.auth.UserAuthentication;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stampbooks")
public class StampBookController {

    private final StampBookService stampBookService;

    // 유저의 모든 스탬프북 조회
    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyStampBooks(@AuthenticationPrincipal Long userId) {
        List<StampBookResponseDTO> response = stampBookService.getStampBooksForUser(userId);
        return SuccessResponse.ok(response);
    }

    // 스탬프북 다운로드 (생성하기)
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createStampBook(@AuthenticationPrincipal Long userId,
                                                              @RequestBody StampBookCreateRequestDTO request) {
        StampBookResponseDTO response = stampBookService.createStampBook(userId, request.cafeId());
        return SuccessResponse.created(response);
    }

}
