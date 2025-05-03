package nova.backend.domain.stampBook.dto.request;

public record UseRewardsRequestDTO(
        Long cafeId, // OWNER와 CAFE는 1:N 관계라서 jwt로 카페 정보를 자동으로 가져올 수 없음.
        String qrCodeValue,
        int count
) {}
