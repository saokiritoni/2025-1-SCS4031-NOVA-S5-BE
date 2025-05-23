package nova.backend.domain.challenge.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.dto.request.ChallengeCreateRequestDTO;
import nova.backend.domain.challenge.dto.response.*;
import nova.backend.domain.challenge.service.OwnerChallengeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/challenges")
@Tag(name = "6. 챌린지 API", description = "챌린지 생성 및 조회")
public class OwnerChallengeController implements OwnerChallengeApi {

    private final OwnerChallengeService challengeService;

    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createChallenge(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChallengeCreateRequestDTO request
    ) {
        challengeService.createChallenge(userDetails.getUserId(), userDetails.getSelectedCafeId(), request);
        return SuccessResponse.created(null);
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<SuccessResponse<?>> getChallengeDetail(
            @PathVariable Long challengeId
    ) {
        ChallengeDetailResponseDTO response = challengeService.getChallengeDetail(challengeId);
        return SuccessResponse.ok(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<SuccessResponse<?>> getUpcomingChallenges(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ChallengeSummaryDTO> response = challengeService.getUpcomingChallenges(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(response);
    }

    @GetMapping("/ongoing")
    public ResponseEntity<SuccessResponse<?>> getOngoingChallenges(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ChallengeSummaryDTO> response = challengeService.getOngoingChallenges(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(response);
    }

    @GetMapping("/completed")
    public ResponseEntity<SuccessResponse<?>> getCompletedChallenges(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<CompletedChallengeListResponseDTO> response = challengeService.getCompletedChallenges(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(response);
    }
}
