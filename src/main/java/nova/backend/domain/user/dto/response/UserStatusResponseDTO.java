package nova.backend.domain.user.dto.response;

import nova.backend.domain.user.entity.User;

public record UserStatusResponseDTO(
        String name,
        String profileImageUrl,
        int todayStampCount,
        int unusedRewardCount
) {

    public static UserStatusResponseDTO from(User user, int todayStampCount, int unusedRewardCount) {
        return new UserStatusResponseDTO(
                user.getName(),
                user.getProfileImageUrl(),
                todayStampCount,
                unusedRewardCount
        );
    }
}

