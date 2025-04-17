package nova.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.SocialType;

public record UserLoginRequestDTO(
        @NotBlank String code,
        @NotNull SocialType socialType,
        @NotNull Role role,
        String name
) {}
