package nova.backend.domain.user.dto.response;

public record UserStatusResponseDTO(
        String name,
        int todayStampCount,
        int unusedRewardCount
) {
    public static UserStatusResponseDTO of(String name, int todayStampCount, int unusedRewardCount) {
        return new UserStatusResponseDTO(name, todayStampCount, unusedRewardCount);
    }
}
