package nova.backend.domain.cafe.dto.common;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeOpenHour;
import nova.backend.domain.cafe.entity.CafeSpecialDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CafeOperatingInfoDTO {

    public static boolean checkIsOpenNow(Cafe cafe) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dow = today.getDayOfWeek();

        Optional<Boolean> isOpenSpecial = cafe.getSpecialDays().stream()
                .filter(d -> d.getSpecialDate().equals(today))
                .map(sp -> sp.isOpen()
                        && !now.isBefore(sp.getOpenTime())
                        && !now.isAfter(sp.getCloseTime()))
                .findFirst();

        return isOpenSpecial.orElseGet(() ->
                cafe.getOpenHours().stream()
                        .filter(h -> h.getDayOfWeek() == dow && h.isOpen())
                        .anyMatch(h -> !now.isBefore(h.getOpenTime()) && !now.isAfter(h.getCloseTime()))
        );
    }

    public static List<CafeOpenHourDTO> openHoursFrom(Cafe cafe) {
        return cafe.getOpenHours().stream()
                .map(h -> new CafeOpenHourDTO(
                        h.getDayOfWeek(),
                        h.isOpen(),
                        h.getOpenTime(),
                        h.getCloseTime(),
                        h.getLastOrder()
                ))
                .collect(Collectors.toList());
    }

    public static List<CafeSpecialDayDTO> specialDaysFrom(Cafe cafe) {
        return cafe.getSpecialDays().stream()
                .map(d -> new CafeSpecialDayDTO(
                        d.getSpecialDate(),
                        d.isOpen(),
                        d.getOpenTime(),
                        d.getCloseTime(),
                        d.getLastOrder(),
                        d.getNote()
                ))
                .collect(Collectors.toList());
    }
}
