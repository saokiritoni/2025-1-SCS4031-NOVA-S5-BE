package nova.backend.domain.stamp.dto.response;

import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stampBook.entity.StampBook;

import java.time.LocalDateTime;
import java.util.List;

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
) {
    public static StampHistoryResponseDTO fromEntity(StampBook stampBook, List<Stamp> stamps) {
        List<LocalDateTime> dates = stamps.stream()
                .map(Stamp::getCreatedAt)
                .toList();

        return new StampHistoryResponseDTO(
                stampBook.getStampBookId(),
                stampBook.getCafe().getCafeName(),
                dates,
                dates.size(),
                stampBook.getCafe().getMaxStampCount(),
                stampBook.isCompleted(),
                stampBook.isCompleted() ? stampBook.getUpdatedAt() : null,
                stampBook.isRewardClaimed(),
                stampBook.isRewardClaimed() ? stampBook.getUpdatedAt() : null
        );
    }
}
