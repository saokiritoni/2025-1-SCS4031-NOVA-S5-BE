package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.response.CafeMyListItemDTO;
import nova.backend.domain.cafe.dto.response.CafeOwnerSelectedResponseDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.domain.cafe.repository.StampBookDesignRepository;
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
    private final StampBookDesignRepository stampBookDesignRepository;


    public void selectCafe(Long userId, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        boolean hasAccess = cafeStaffRepository.existsByCafe_CafeIdAndUser_UserId(cafeId, userId);
        if (!hasAccess) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        redisTemplate.opsForValue().set("selectedCafe:" + userId, cafeId.toString());
    }

    public List<CafeMyListItemDTO> getMyCafes(Long userId, Long selectedCafeId) {
        List<Cafe> cafes = cafeRepository.findAllByUserId(userId);

        return cafes.stream()
                .map(cafe -> CafeMyListItemDTO.from(
                        cafe,
                        cafe.getCafeId().equals(selectedCafeId)
                ))
                .toList();
    }


    public CafeOwnerSelectedResponseDTO getSelectedCafe(Long selectedCafeId) {
        Cafe cafe = cafeRepository.findById(selectedCafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        boolean hasStampBookDesign = stampBookDesignRepository.existsByCafe_CafeId(selectedCafeId);

        return CafeOwnerSelectedResponseDTO.fromEntity(cafe, hasStampBookDesign);
    }

}
