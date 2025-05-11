package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeListResponseDTO;
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
        // 카페 존재 확인
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // user가 이 cafe의 owner or staff인지 확인
        boolean hasAccess = cafeStaffRepository.existsByCafe_CafeIdAndUser_UserId(cafeId, userId);

        if (!hasAccess) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        redisTemplate.opsForValue().set("selectedCafe:" + userId, cafeId.toString());
    }

    public List<CafeListResponseDTO> getMyCafes(Long userId) {
        return cafeStaffRepository.findByUser_UserId(userId).stream()
                .map(CafeStaff::getCafe)
                .map(CafeListResponseDTO::fromEntity)
                .toList();
    }

    public CafeListResponseDTO getSelectedCafe(Long selectedCafeId) {
        Cafe cafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return CafeListResponseDTO.fromEntity(cafe);
    }


}


