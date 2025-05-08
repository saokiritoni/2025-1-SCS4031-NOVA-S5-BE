package nova.backend.global.auth;

import lombok.Getter;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final Role role;
    private final User user;
    private final Long selectedCafeId;

    public CustomUserDetails(User user, Long selectedCafeId) {
        this.user = user;
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.selectedCafeId = selectedCafeId;
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인이라면 null
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getSelectedCafeId() { return selectedCafeId; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
