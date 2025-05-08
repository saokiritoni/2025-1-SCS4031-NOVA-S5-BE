package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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

}


