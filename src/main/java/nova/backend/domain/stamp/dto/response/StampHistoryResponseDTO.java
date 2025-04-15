package nova.backend.domain.stamp.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record StampHistoryResponseDTO(
        Long stampBookId,
        String cafeName,
        List<LocalDateTime> stampDates,
        int stampCount,
        int maxStampCount,
        boolean isCompleted,
        LocalDateTime completedAt,
        boolean rewardClaimed,
        LocalDateTime rewardClaimedAt
) {}