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
        List<CafeSpecialDayDTO> specialDays,
        Integer downloadCount
) {

    // 기존 사용: downloadCount 없이 기본값 0으로 설정
    public static CafeListResponseDTO fromEntity(Cafe cafe) {
        return fromEntityWithDownloadCount(cafe, 0);
    }

    // 다운로드 수가 명시적으로 주어지는 경우 사용
    public static CafeListResponseDTO fromEntityWithDownloadCount(Cafe cafe, Integer downloadCount) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dow = today.getDayOfWeek();

        // 특별 영업일 우선 확인
        Optional<Boolean> isOpenSpecial = cafe.getSpecialDays().stream()
                .filter(d -> d.getSpecialDate().equals(today))
                .map(sp -> sp.isOpen()
                        && !now.isBefore(sp.getOpenTime())
                        && !now.isAfter(sp.getCloseTime()))
                .findFirst();

        // 특별 영업일이 없을 경우 일반 요일 영업시간 체크
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
                sdDtos,
                downloadCount != null ? downloadCount : 0
        );
    }

    public record CafeOpenHourDTO(
            DayOfWeek dayOfWeek,
            boolean isOpen,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime lastOrder
    ) {}

    public record CafeSpecialDayDTO(
            LocalDate specialDate,
            boolean isOpen,
            LocalTime openTime,
            LocalTime closeTime,
            LocalTime lastOrder,
            String note
    ) {}
}
