package nova.backend.domain.stamp.dto.response;

import java.util.List;

public record StaffStampViewResponseDTO(
        List<StampHistoryResponseDTO> history,
        List<RecentStampResponseDTO> recentStamps
) {
}
