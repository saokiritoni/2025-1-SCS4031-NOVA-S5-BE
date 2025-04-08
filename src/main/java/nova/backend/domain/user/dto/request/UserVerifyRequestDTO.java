package nova.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserVerifyRequestDTO(
        @NotBlank
        String studentNumber
) {
}
