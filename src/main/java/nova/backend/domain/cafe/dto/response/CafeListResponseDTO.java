package nova.backend.domain.cafe.dto.response;

import lombok.Builder;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeOpenHour;
import nova.backend.domain.cafe.entity.CafeSpecialDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
public record CafeListResponseDTO(
        Long cafeId,
        String cafeName,
        Double latitude,
        Double longitude,
        String cafePhone,
        Integer maxStampCount,
        boolean isOpenNow,
        List<CafeOpenHourDTO> openHours,
        List<CafeSpecialDayDTO> specialDays
) {

    public static CafeListResponseDTO fromEntity(Cafe cafe) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dow = today.getDayOfWeek();

        // Special day check
        Optional<Boolean> isOpenSpecial = cafe.getSpecialDays().stream()
                .filter(d -> d.getSpecialDate().equals(today))
                .map(sp -> sp.isOpen()
                        && !now.isBefore(sp.getOpenTime())
                        && !now.isAfter(sp.getCloseTime()))
                .findFirst();

        boolean isOpenNow = isOpenSpecial.orElseGet(() ->
                cafe.getOpenHours().stream()
                        .filter(h -> h.getDayOfWeek() == dow && h.isOpen())
                        .anyMatch(h -> !now.isBefore(h.getOpenTime()) && !now.isAfter(h.getCloseTime()))
        );

        List<CafeOpenHourDTO> ohDtos = cafe.getOpenHours().stream()
                .map(h -> new CafeOpenHourDTO(
                        h.getDayOfWeek(),
                        h.isOpen(),
                        h.getOpenTime(),
                        h.getCloseTime(),
                        h.getLastOrder()
                ))
                .collect(Collectors.toList());

        List<CafeSpecialDayDTO> sdDtos = cafe.getSpecialDays().stream()
                .map(d -> new CafeSpecialDayDTO(
                        d.getSpecialDate(),
                        d.isOpen(),
                        d.getOpenTime(),
                        d.getCloseTime(),
                        d.getLastOrder(),
                        d.getNote()
                ))
                .collect(Collectors.toList());

        return new CafeListResponseDTO(
                cafe.getCafeId(),
                cafe.getCafeName(),
                cafe.getLatitude(),
                cafe.getLongitude(),
                cafe.getCafePhone(),
                cafe.getMaxStampCount(),
                isOpenNow,
                ohDtos,
                sdDtos
        );
    }

    public record CafeOpenHourDTO(DayOfWeek dayOfWeek, boolean isOpen, LocalTime openTime, LocalTime closeTime, LocalTime lastOrder) {}
    public record CafeSpecialDayDTO(LocalDate specialDate, boolean isOpen, LocalTime openTime, LocalTime closeTime, LocalTime lastOrder, String note) {}
}
