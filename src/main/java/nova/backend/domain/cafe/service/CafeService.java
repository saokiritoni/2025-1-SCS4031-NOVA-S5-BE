package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeRepository cafeRepository;

    public List<CafeListResponseDTO> getAllCafes() {
        List<Cafe> cafes = cafeRepository.findAll();
        return cafes.stream()
                .map(c -> new CafeListResponseDTO(
                        c.getCafeId(),
                        c.getCafeName(),
                        c.getLatitude(),
                        c.getLongitude(),
                        c.getCafePhone(),
                        c.getMaxStampCount()
                ))
                .toList();
    }
}
