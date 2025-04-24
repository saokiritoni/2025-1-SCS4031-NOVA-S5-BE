package nova.backend.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.auth.CustomUserDetailsService;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.UnauthorizedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // TODO: 로그 삭제하기

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("[JwtAuthFilter] 요청 URI: {}", request.getRequestURI());

            final String accessToken = getAccessTokenFromHttpServletRequest(request);
            log.info("[JwtAuthFilter] 추출된 AccessToken: {}", accessToken);

            jwtProvider.validateAccessToken(accessToken);
            log.info("[JwtAuthFilter] AccessToken 유효성 검사 통과");

            final Long userId = jwtProvider.getSubject(accessToken);
            log.info("[JwtAuthFilter] 토큰에서 추출한 userId: {}", userId);

            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userId.toString());
            log.info("[JwtAuthFilter] 로드된 사용자 email={}, role={}, authorities={}",
                    userDetails.getEmail(), userDetails.getRole(), userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("[JwtAuthFilter] SecurityContextHolder 인증 객체 설정 완료");

        } catch (UnauthorizedException e) {
            log.warn("[JwtAuthFilter] 인증 실패 - {}", e.getMessage());
            setErrorResponse(response, e.getErrorCode());
            return;
        } catch (Exception e) {
            log.error("[JwtAuthFilter] 알 수 없는 예외 발생", e);
            setErrorResponse(response, ErrorCode.AUTHENTICATION_FAILED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        log.info("[JwtAuthFilter] Authorization 헤더: {}", accessToken);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(BEARER.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", errorCode.getHttpStatus().value());
        errorBody.put("message", errorCode.getMessage());

        objectMapper.writeValue(response.getWriter(), errorBody);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/reissue") ||
                path.startsWith("/api/auth/token") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs");
    }
}
