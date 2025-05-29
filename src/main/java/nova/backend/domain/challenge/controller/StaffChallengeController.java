package nova.backend.domain.challenge.controller;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.challenge.controller.api.StaffChallengeApi;
import nova.backend.domain.challenge.dto.request.ChallengeAccumulateRequestDTO;
import nova.backend.domain.challenge.service.StaffChallengeService;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges/staff")
public class StaffChallengeController implements StaffChallengeApi {

    private final StaffChallengeService staffChallengeService;

    @PostMapping("/accumulate")
    @PreAuthorize("hasRole('STAFF') or hasRole('OWNER')")
    public ResponseEntity<SuccessResponse<?>> accumulateChallenge(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChallengeAccumulateRequestDTO request
    ) {
        staffChallengeService.accumulateOngoingChallengeForCafe(
                userDetails.getSelectedCafeId(),
                request.qrCodeValue()
        );
        return SuccessResponse.created(null);
    }

}

