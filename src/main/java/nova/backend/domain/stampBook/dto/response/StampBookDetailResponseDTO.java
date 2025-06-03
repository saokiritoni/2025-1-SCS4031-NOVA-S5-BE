package nova.backend.domain.stampBook.dto.response;

import nova.backend.domain.cafe.dto.response.CafeDesignOverviewDTO;

public record StampBookDetailResponseDTO(
        CafeDesignOverviewDTO cafeDesignOverview,
        StampBookResponseDTO stampBookInfo,
        int rewardAvailableCount
) {
    public static StampBookDetailResponseDTO of(CafeDesignOverviewDTO overview, StampBookResponseDTO stampBook, int rewardAvailableCount) {
        return new StampBookDetailResponseDTO(overview, stampBook, rewardAvailableCount);
    }
}
