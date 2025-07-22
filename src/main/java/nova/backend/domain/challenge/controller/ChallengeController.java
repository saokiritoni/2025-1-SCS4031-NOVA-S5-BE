package nova.backend.domain.challenge.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.controller.api.ChallengeApi;
import nova.backend.domain.challenge.dto.common.ChallengeBaseDTO;
import nova.backend.domain.challenge.dto.response.ChallengeSummaryDTO;
import nova.backend.domain.challenge.entity.status.ChallengeStatus;
import nova.backend.domain.challenge.service.ChallengeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges")
public class ChallengeController implements ChallengeApi {

    private final ChallengeService challengeService;

    @PatchMapping("/{challengeId}/withdraw")
    public ResponseEntity<SuccessResponse<?>> withdrawChallenge(
            @PathVariable Long challengeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        challengeService.withdrawChallenge(challengeId, userDetails.getUserId());
        return SuccessResponse.ok(null);
    }

    @GetMapping("/browse")
    public ResponseEntity<SuccessResponse<?>> getAvailableChallenges() {
        List<ChallengeSummaryDTO> response = challengeService.getAvailableChallenges();
        return SuccessResponse.ok(response);
    }


    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyChallenges(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam ChallengeStatus status
    ) {
        List<ChallengeSummaryDTO> response = challengeService.getMyChallenges(userDetails.getUserId(), status);
        return SuccessResponse.ok(response);
    }


    @GetMapping("/{challengeId}")
    public ResponseEntity<SuccessResponse<?>> getChallengeDetail(
            @PathVariable Long challengeId
    ) {
        ChallengeBaseDTO response = challengeService.getChallengeDetail(challengeId);
        return SuccessResponse.ok(response);
    }
}
