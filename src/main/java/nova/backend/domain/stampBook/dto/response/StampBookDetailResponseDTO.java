package nova.backend.domain.stampBook.dto.response;

import nova.backend.domain.cafe.dto.response.CafeDesignOverviewDTO;

public record StampBookDetailResponseDTO(
        CafeDesignOverviewDTO cafeDesignOverview,
        StampBookResponseDTO stampBookInfo
) {
    public static StampBookDetailResponseDTO of(CafeDesignOverviewDTO overview, StampBookResponseDTO stampBook) {
        return new StampBookDetailResponseDTO(overview, stampBook);
    }
}
