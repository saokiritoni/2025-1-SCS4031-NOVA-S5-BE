package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.dto.request.StampBookDesignCreateRequestDTO;
import nova.backend.domain.cafe.dto.response.StampBookDesignDetailDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeStaff;
import nova.backend.domain.cafe.entity.StampBookDesign;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.domain.cafe.repository.StampBookDesignRepository;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import nova.backend.global.util.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static nova.backend.global.error.ErrorCode.ACCESS_DENIED;
import static nova.backend.global.error.ErrorCode.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerCafeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final CafeStaffRepository cafeStaffRepository;
    private final StampBookDesignRepository stampBookDesignRepository;
    private final EmailService emailService;

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

    @Transactional
    public void addStampBookDesign(Long ownerId, Long cafeId, StampBookDesignCreateRequestDTO request) {
        Cafe cafe = getOwnedCafe(ownerId, cafeId);
        StampBookDesign design = request.toEntity(cafe);
        stampBookDesignRepository.save(design);
    }


    @Transactional
    public void setExposedStampBookDesign(Long ownerId, Long cafeId, Long designId) {
        Cafe cafe = getOwnedCafe(ownerId, cafeId);

        // 모든 기존 디자인 노출 비활성화
        List<StampBookDesign> designs = cafe.getStampBookDesigns();
        for (StampBookDesign design : designs) {
            design.unexpose();
        }

        // 노출할 디자인만 true 설정
        StampBookDesign designToExpose = designs.stream()
                .filter(d -> d.getDesignId().equals(designId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        designToExpose.expose();
    }

    @Transactional(readOnly = true)
    public StampBookDesignDetailDTO getExposedStampBookDesign(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        return cafe.getStampBookDesigns().stream()
                .filter(StampBookDesign::isExposed)
                .findFirst()
                .map(StampBookDesignDetailDTO::fromEntity)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<StampBookDesignDetailDTO> getAllStampBookDesigns(Long ownerId, Long cafeId) {
        Cafe cafe = getOwnedCafe(ownerId, cafeId);
        return cafe.getStampBookDesigns().stream()
                .map(StampBookDesignDetailDTO::fromEntity)
                .toList();
    }

    private Cafe getOwnedCafe(Long ownerId, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (!cafe.getOwner().getUserId().equals(ownerId)) {
            throw new BusinessException(ACCESS_DENIED);
        }
        return cafe;
    }

    @Transactional(readOnly = true)
    public StampBookDesign getStampBookDesignById(Long ownerId, Long cafeId, Long designId) {
        Cafe cafe = getOwnedCafe(ownerId, cafeId);

        return cafe.getStampBookDesigns().stream()
                .filter(d -> d.getDesignId().equals(designId))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));
    }

}