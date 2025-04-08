package nova.backend.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nova.backend.global.auth.UserAuthentication;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.UnauthorizedException;
import nova.backend.domain.user.entity.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = getAccessTokenFromHttpServletRequest(request);
        jwtProvider.validateAccessToken(accessToken);
        final Long userId = jwtProvider.getSubject(accessToken);
        final Role role = jwtProvider.getRole(accessToken);
        setAuthentication(request, userId, role);
        filterChain.doFilter(request, response);
    }

    // 로그인 관련 경로는 필터 적용 제외
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/reissue") ||
                path.startsWith("/api/auth/token");
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(BEARER.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
    }

    private void setAuthentication(HttpServletRequest request, Long userId, Role role) {
        UserAuthentication authentication = new UserAuthentication(userId, null, List.of(role));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
