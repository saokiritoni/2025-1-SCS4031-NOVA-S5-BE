package nova.backend.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.user.entity.User;
import nova.backend.global.auth.CustomUserDetails;
import nova.backend.global.auth.CustomUserDetailsService;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.UnauthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 인증 생략 필요한 정확한 경로 지정
    private static final List<String> exactSkipPaths = List.of(
            "/",
            "/swagger-ui",
            "/v3/api-docs",
            "/auth/callback",
            "/api/auth/login"
    );

    // 인증 생략 필요한 패턴 경로 지정
    private static final List<String> patternSkipPaths = List.of(
            "/api/cafes/**",
            "/api/auth/token/**"
    );



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

            // User 엔티티 직접 조회
            User user = userDetailsService.loadUserEntityById(userId);
            log.info("[JwtAuthFilter] 로드된 사용자 email={}, role={}", user.getEmail(), user.getRole());

            // Redis에서 selectedCafeId 조회
            String redisKey = "selectedCafe:" + userId;
            String cafeIdStr = redisTemplate.opsForValue().get(redisKey);
            Long selectedCafeId = cafeIdStr != null ? Long.valueOf(cafeIdStr) : null;
            log.info("[JwtAuthFilter] Redis에서 가져온 selectedCafeId: {}", selectedCafeId);

            // CustomUserDetails에 selectedCafeId 포함
            CustomUserDetails userDetails = new CustomUserDetails(user, selectedCafeId);

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
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("[JwtAuthFilter] 현재 요청 URI = {}", path);

        if (exactSkipPaths.contains(path)) {
            return true;
        }
        
        for (String pattern : patternSkipPaths) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }

        return false;
    }

}

