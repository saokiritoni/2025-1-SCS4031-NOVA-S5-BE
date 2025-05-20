package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.dto.response.CafeWithDownloadCountDTO;
import nova.backend.domain.cafe.dto.response.PopularCafeResponseDTO;
import nova.backend.domain.cafe.entity.*;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
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
    private final UserRepository userRepository;
    private final CafeStaffRepository cafeStaffRepository;

    public List<CafeListResponseDTO> getAllCafes() {
        return cafeRepository.findAll().stream()
                .map(CafeListResponseDTO::fromEntity)
                .toList();
    }

    public List<CafeListResponseDTO> getApprovedCafes() {
        return cafeRepository.findByRegistrationStatus(CafeRegistrationStatus.APPROVED).stream()
                .map(CafeListResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public Cafe registerCafe(Long ownerId, CafeRegistrationRequestDTO request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe cafe = Cafe.builder()
                .cafeName(request.cafeName())
                .branchName(request.branchName())
                .ownerName(request.ownerName())
                .ownerPhone(request.ownerPhone())
                .businessNumber(request.businessNumber())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .maxStampCount(request.maxStampCount())
                .characterType(request.characterType())
                .rewardDescription(request.rewardDescription())
                .registrationStatus(CafeRegistrationStatus.REQUESTED)
                .owner(owner)
                .build();

        Cafe savedCafe = cafeRepository.save(cafe);
        CafeStaff cafeStaff = CafeStaff.builder()
                .cafe(savedCafe)
                .user(owner)
                .role(Role.OWNER)
                .build();

        cafeStaffRepository.save(cafeStaff);

        return savedCafe;
    }

    public static CafeListResponseDTO fromEntityWithDownloadCount(Cafe cafe, Integer downloadCount) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DayOfWeek dow = today.getDayOfWeek();

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

        List<CafeListResponseDTO.CafeOpenHourDTO> ohDtos = cafe.getOpenHours().stream()
                .map(h -> new CafeListResponseDTO.CafeOpenHourDTO(
                        h.getDayOfWeek(),
                        h.isOpen(),
                        h.getOpenTime(),
                        h.getCloseTime(),
                        h.getLastOrder()
                ))
                .collect(Collectors.toList());

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

    public List<PopularCafeResponseDTO> getTop10CafesByStampBookDownload() {
        List<CafeWithDownloadCountDTO> results = cafeRepository.findTop10CafesByStampBookCount(PageRequest.of(0, 10));

        return results.stream()
                .map(PopularCafeResponseDTO::from)
                .toList();
    }




}
