package nova.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    USER("일반 회원"),
    OWNER("카페 사장"),
    STAFF("카페 직원");

    private final String title;

    @Override
    public String getAuthority() {
        return name();
    }
}
