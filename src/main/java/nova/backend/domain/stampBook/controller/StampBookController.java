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

    // 스탬프북 리워드로 전환
    @PostMapping("/{stampBookId}/reward")
    public ResponseEntity<SuccessResponse<?>> convertToReward(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        String reward = stampBookService.convertStampBookToReward(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("리워드 전환 완료: " + reward);
    }

    // 마이페이지에 스탬프북 추가
    @PostMapping("/{stampBookId}/home")
    public ResponseEntity<SuccessResponse<?>> addStampBookToHome(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        stampBookService.addStampBookToHome(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("마이페이지에 추가되었습니다.");
    }

    // 마이페이지에서 스탬프북 제거
    @DeleteMapping("/{stampBookId}/home")
    public ResponseEntity<SuccessResponse<?>> removeStampBookFromHome(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long stampBookId
    ) {
        stampBookService.removeStampBookFromHome(userDetails.getUserId(), stampBookId);
        return SuccessResponse.ok("마이페이지에서 제거되었습니다.");
    }

    // 메인페이지용 스탬프북 조회 (inhome)
    @GetMapping("/my/home")
    public ResponseEntity<SuccessResponse<?>> getMyHomeStampBooks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<StampBookResponseDTO> response = stampBookService.getHomeStampBooksForUser(userDetails.getUserId());
        return SuccessResponse.ok(response);
    }

}
