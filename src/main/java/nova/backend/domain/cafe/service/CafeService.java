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
import nova.backend.global.util.EmailService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final EmailService emailService;

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
    public Cafe registerCafe(Long ownerId, CafeRegistrationRequestDTO request, MultipartFile businessRegistrationPdf) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe savedCafe = cafeRepository.save(request.toEntity(owner));

        CafeStaff cafeStaff = CafeStaff.builder()
                .cafe(savedCafe)
                .user(owner)
                .role(Role.OWNER)
                .build();

        cafeStaffRepository.save(cafeStaff);
        emailService.sendCafeRegistrationEmail(savedCafe, businessRegistrationPdf);

        return savedCafe;
    }


    public List<PopularCafeResponseDTO> getTop10CafesByStampBookDownload() {
        List<CafeWithDownloadCountDTO> results = cafeRepository.findTop10CafesByStampBookCount(PageRequest.of(0, 10));

        return results.stream()
                .map(PopularCafeResponseDTO::from)
                .toList();
    }

}
