package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
import nova.backend.domain.cafe.dto.response.CafeSelectedResponseDTO;
import nova.backend.domain.cafe.dto.response.MyCafeSimpleResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeStaff;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeSelectionService {

    private final RedisTemplate<String, String> redisTemplate;
    private final CafeRepository cafeRepository;
    private final CafeStaffRepository cafeStaffRepository;

    public void selectCafe(Long userId, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        boolean hasAccess = cafeStaffRepository.existsByCafe_CafeIdAndUser_UserId(cafeId, userId);
        if (!hasAccess) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        redisTemplate.opsForValue().set("selectedCafe:" + userId, cafeId.toString());
    }

    public List<MyCafeSimpleResponseDTO> getMyCafes(Long userId, Long selectedCafeId) {
        List<Cafe> cafes = cafeRepository.findAllByUserId(userId);

        return cafes.stream()
                .map(cafe -> MyCafeSimpleResponseDTO.from(
                        cafe,
                        cafe.getCafeId().equals(selectedCafeId)
                ))
                .toList();
    }

    public CafeSelectedResponseDTO getSelectedCafe(Long selectedCafeId) {
        Cafe cafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return CafeSelectedResponseDTO.fromEntity(cafe);
    }
}
