package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeDesignOverviewDTO;
import nova.backend.domain.cafe.dto.response.CafeSummaryWithConceptDTO;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    /**
    CafeService에서는 모두 같은 DTO를 사용하되, 조건에 따라서만 다른 응답을 반환합니다.
     */

    private final CafeRepository cafeRepository;

    /**
     * 모든 카페 목록 조회 (기본 정보 + 컨셉 소개)
     */
    public List<CafeSummaryWithConceptDTO> getAllCafes() {
        return cafeRepository.findAll().stream()
                .map(CafeSummaryWithConceptDTO::from)
                .toList();
    }

    /**
     * 승인된 카페 목록 조회 (기본 정보 + 컨셉 소개)
     */
    public List<CafeSummaryWithConceptDTO> getApprovedCafes() {
        return cafeRepository.findByRegistrationStatus(CafeRegistrationStatus.APPROVED).stream()
                .map(CafeSummaryWithConceptDTO::from)
                .toList();
    }

    /**
     * 다운로드 수 기준 인기 카페 TOP 10 (기본 정보 + 컨셉 소개)
     */
    public List<CafeSummaryWithConceptDTO> getTop10CafesByStampBookDownload() {
        return cafeRepository.findTop10CafesByStampBookCount(PageRequest.of(0, 10)).stream()
                .map(dto -> CafeSummaryWithConceptDTO.from(dto.cafe()))
                .toList();
    }

    /**
    * 단일 카페 조회
     */
    @Transactional(readOnly = true)
    public CafeDesignOverviewDTO getCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .map(CafeDesignOverviewDTO::fromEntity)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
    }

}

