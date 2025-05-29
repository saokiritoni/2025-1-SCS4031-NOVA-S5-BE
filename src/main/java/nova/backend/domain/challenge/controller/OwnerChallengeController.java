package nova.backend.domain.challenge.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.controller.api.OwnerChallengeApi;
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
        OwnerChallengeDetailResponseDTO response = challengeService.getChallengeDetail(challengeId);
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
        List<OwnerCompletedChallengeListResponseDTO> response = challengeService.getCompletedChallenges(userDetails.getSelectedCafeId());
        return SuccessResponse.ok(response);
    }
}
