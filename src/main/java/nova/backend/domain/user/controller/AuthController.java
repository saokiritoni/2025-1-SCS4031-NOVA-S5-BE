package nova.backend.domain.user.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nova.backend.global.common.SuccessResponse;
import nova.backend.domain.user.dto.request.UserLoginRequestDTO;
import nova.backend.domain.user.dto.request.UserTokenRequestDTO;
import nova.backend.domain.user.dto.response.UserTokenResponseDTO;
import nova.backend.domain.user.service.OauthService;
import nova.backend.domain.user.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController implements AuthApi {

    private final OauthService oauthService;
    private final TokenService tokenService;

    // 임시 토큰 발급 API. 추후 로그인 기능이 완성되면 삭제할 예정
    @PostMapping("/token/{userId}")
    public ResponseEntity<SuccessResponse<?>> getTempToken(@PathVariable Long userId) {
        UserTokenResponseDTO userToken = tokenService.issueTempToken(userId);
        return SuccessResponse.ok(userToken);
    }

    // 소셜 로그인
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<?>> socialLogin(@RequestBody @Valid UserLoginRequestDTO userLoginRequest) {
        UserTokenResponseDTO userToken = oauthService.socialLogin(userLoginRequest);
        return SuccessResponse.ok(userToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<?>> logout(@AuthenticationPrincipal Long userId) {
        tokenService.logout(userId);
        return SuccessResponse.ok(null);
    }

    // 리프레쉬 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<?>> reissueToken(@RequestBody @Valid UserTokenRequestDTO uerTokenRequest) {
        UserTokenResponseDTO userToken = tokenService.reissue(uerTokenRequest);
        return SuccessResponse.ok(userToken);
    }

}