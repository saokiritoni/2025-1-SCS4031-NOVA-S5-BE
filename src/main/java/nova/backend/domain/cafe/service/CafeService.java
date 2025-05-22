package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.dto.response.CafeWithDownloadCountDTO;
import nova.backend.domain.cafe.dto.response.PopularCafeResponseDTO;
import nova.backend.domain.cafe.entity.*;
import nova.backend.domain.cafe.repository.CafeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;

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

    public List<PopularCafeResponseDTO> getTop10CafesByStampBookDownload() {
        List<CafeWithDownloadCountDTO> results = cafeRepository.findTop10CafesByStampBookCount(PageRequest.of(0, 10));

        return results.stream()
                .map(PopularCafeResponseDTO::from)
                .toList();
    }

}
