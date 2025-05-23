package nova.backend.domain.cafe.dto.common;

import java.time.LocalDate;
import java.time.LocalTime;

public record CafeSpecialDayDTO(
        LocalDate specialDate,
        boolean isOpen,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime lastOrder,
        String note
) {}
