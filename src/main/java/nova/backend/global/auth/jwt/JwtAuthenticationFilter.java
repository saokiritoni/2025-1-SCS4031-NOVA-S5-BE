package nova.backend.global.auth.jwt;

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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String accessToken = getAccessTokenFromHttpServletRequest(request);
            jwtProvider.validateAccessToken(accessToken);

            final Long userId = jwtProvider.getSubject(accessToken);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userId.toString());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (UnauthorizedException e) {
            log.warn("❌ JWT 인증 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("🔥 JWT 필터 처리 중 예외 발생", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(BEARER.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
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
