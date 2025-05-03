package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeOpenHour;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import nova.backend.domain.cafe.entity.CafeSpecialDay;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
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

        return cafeRepository.save(cafe);
    }
}
