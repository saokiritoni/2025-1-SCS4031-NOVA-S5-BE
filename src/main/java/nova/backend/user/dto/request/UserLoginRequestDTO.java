package nova.backend.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nova.backend.user.entity.SocialType;

public record UserLoginRequestDTO(
        @NotBlank String code,
        @NotNull SocialType socialType
) {}
