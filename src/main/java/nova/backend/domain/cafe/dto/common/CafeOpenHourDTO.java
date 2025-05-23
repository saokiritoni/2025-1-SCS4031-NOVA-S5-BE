package nova.backend.domain.cafe.dto.common;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record CafeOpenHourDTO(
        DayOfWeek dayOfWeek,
        boolean isOpen,
        LocalTime openTime,
        LocalTime closeTime,
        LocalTime lastOrder
) {}
