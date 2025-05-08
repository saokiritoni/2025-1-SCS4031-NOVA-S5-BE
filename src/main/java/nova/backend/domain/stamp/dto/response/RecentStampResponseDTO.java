package nova.backend.domain.stamp.dto.response;

import nova.backend.domain.stamp.entity.Stamp;

import java.time.LocalDateTime;

public record RecentStampResponseDTO(
        Long stampId,
        Long stampBookId,
        String userName,
        LocalDateTime createdAt
) {
    public static RecentStampResponseDTO fromEntity(Stamp stamp) {
        return new RecentStampResponseDTO(
                stamp.getStampId(),
                stamp.getStampBook().getStampBookId(),
                stamp.getStampBook().getUser().getName(),
                stamp.getCreatedAt()
        );
    }
}
