package nova.backend.domain.stampBook.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.stampBook.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.service.UserStampBookService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stampbooks")
public class StampBookController {

    private final UserStampBookService userStampBookService;

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyStampBooks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<StampBookResponseDTO> response = userStampBookService.getStampBooksForUser(userDetails.getUserId());
        return SuccessResponse.ok(response);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> downloadStampBook(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StampBookCreateRequestDTO request
    ) {
        StampBookResponseDTO response = userStampBookService.createStampBook(userDetails.getUserId(), request.cafeId());
        return SuccessResponse.created(response);
    }

    @PostMapping("/{stampBookId}/reward")
    public ResponseEntity<SuccessResponse<?>> convertToReward(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        String reward = userStampBookService.convertStampBookToReward(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("리워드 전환 완료: " + reward);
    }

    @PostMapping("/{stampBookId}/home")
    public ResponseEntity<SuccessResponse<?>> addStampBookToHome(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        userStampBookService.addStampBookToHome(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("마이페이지에 추가되었습니다.");
    }

    @DeleteMapping("/{stampBookId}/home")
    public ResponseEntity<SuccessResponse<?>> removeStampBookFromHome(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        userStampBookService.removeStampBookFromHome(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("마이페이지에서 제거되었습니다.");
    }

    @GetMapping("/my/home")
    public ResponseEntity<SuccessResponse<?>> getMyHomeStampBooks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<StampBookResponseDTO> response = userStampBookService.getHomeStampBooksForUser(userDetails.getUserId());
        return SuccessResponse.ok(response);
    }
} 