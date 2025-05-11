package nova.backend.domain.stamp.dto.response;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.user.entity.User;

import java.util.List;

public record StaffStampViewResponseDTO(
        String profilePictureUrl,
        String name,
        String characterType, // 카페의 캐릭터
        int rewardCount,
        List<StampHistoryResponseDTO> history,
        List<RecentStampResponseDTO> recentStamps
) {
    public static StaffStampViewResponseDTO from(User user, Cafe cafe, int rewardCount,
                                                 List<StampHistoryResponseDTO> history,
                                                 List<RecentStampResponseDTO> recentStamps) {
        return new StaffStampViewResponseDTO(
                user.getProfileImageUrl(),
                user.getName(),
                cafe.getCharacterType().name(),
                rewardCount,
                history,
                recentStamps
        );
    }
}
