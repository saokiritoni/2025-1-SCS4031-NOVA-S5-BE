package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeOpenHour;
import nova.backend.domain.cafe.entity.CafeSpecialDay;
import nova.backend.domain.cafe.repository.CafeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;

    public List<CafeListResponseDTO> getAllCafes() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dow = today.getDayOfWeek();

        return cafeRepository.findAll().stream().map(cafe -> {
            // --- 평일 스케줄 DTO 리스트 (Open Hour DTOs) ---
            List<CafeListResponseDTO.CafeOpenHourDTO> ohDtos = cafe.getOpenHours().stream()
                    .map(h -> new CafeListResponseDTO.CafeOpenHourDTO(
                            h.getDayOfWeek(),
                            h.isOpen(),
                            h.getOpenTime(),
                            h.getCloseTime(),
                            h.getLastOrder()
                    ))
                    .collect(Collectors.toList());

            // --- 특수일 스케줄 DTO 리스트 (Special Day DTOs) ---
            List<CafeListResponseDTO.CafeSpecialDayDTO> sdDtos = cafe.getSpecialDays().stream()
                    .map(d -> new CafeListResponseDTO.CafeSpecialDayDTO(
                            d.getSpecialDate(),
                            d.isOpen(),
                            d.getOpenTime(),
                            d.getCloseTime(),
                            d.getLastOrder(),
                            d.getNote()
                    ))
                    .collect(Collectors.toList());

            // --- 오늘 영업중 여부 계산 ---
            Optional<CafeSpecialDay> spOpt = cafe.getSpecialDays().stream()
                    .filter(d -> d.getSpecialDate().equals(today))
                    .findFirst();

            boolean isOpenNow = spOpt.map(sp ->
                    sp.isOpen()
                            && !now.isBefore(sp.getOpenTime())
                            && !now.isAfter(sp.getCloseTime())
            ).orElseGet(() ->
                    cafe.getOpenHours().stream()
                            .filter(h -> h.getDayOfWeek() == dow && h.isOpen())
                            .anyMatch(h -> !now.isBefore(h.getOpenTime()) && !now.isAfter(h.getCloseTime()))
            );

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
        }).toList();
    }
}
