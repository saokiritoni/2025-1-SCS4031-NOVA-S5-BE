package nova.backend.domain.challenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.backend.domain.challenge.dto.request.ChallengeCreateRequestDTO;
import nova.backend.domain.challenge.dto.response.*;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "4. 챌린지(OWNER) API", description = "사장용 챌린지 생성 및 조회")
@RequestMapping("/api/owner/challenges")
@SecurityRequirement(name = "token")
public interface OwnerChallengeApi {

    @Operation(summary = "챌린지 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "챌린지 생성 성공"),
            @ApiResponse(responseCode = "403", description = "카페 소유주 아님"),
            @ApiResponse(responseCode = "404", description = "카페를 찾을 수 없음")
    })
    @PostMapping
    ResponseEntity<SuccessResponse<?>> createChallenge(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ChallengeCreateRequestDTO request
    );

    @Operation(summary = "챌린지 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "챌린지를 찾을 수 없음")
    })
    @GetMapping("/{challengeId}")
    ResponseEntity<SuccessResponse<ChallengeDetailResponseDTO>> getChallengeDetail(
            @PathVariable Long challengeId
    );

    @Operation(summary = "진행 예정 챌린지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/upcoming")
    ResponseEntity<SuccessResponse<List<ChallengeSummaryDTO>>> getUpcomingChallenges(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "진행 중 챌린지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/ongoing")
    ResponseEntity<SuccessResponse<List<ChallengeSummaryDTO>>> getOngoingChallenges(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @Operation(summary = "완료된 챌린지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/completed")
    ResponseEntity<SuccessResponse<List<CompletedChallengeListResponseDTO>>> getCompletedChallenges(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    );
}
