package nova.backend.domain.cafe.dto.response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CafeListResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        String cafePhone,
        Integer maxStampCount,
        boolean isOpen,
        List<CafeOpenHourDTO> openHours,
        List<CafeSpecialDayDTO> specialDays
) {
    public record CafeOpenHourDTO(
            DayOfWeek dayOfWeek,
            boolean isOpen,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime lastOrder
    ) {}
    /*
    특수 휴일 처리용 DTO (공휴일, 임시 휴일)
     */
    public record CafeSpecialDayDTO(
            LocalDate specialDate,
            boolean isOpen,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime lastOrder,
            String note
    ) {}
}
