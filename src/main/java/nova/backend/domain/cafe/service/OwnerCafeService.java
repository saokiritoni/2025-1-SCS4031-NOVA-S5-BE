package nova.backend.domain.cafe.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.dto.request.CafeRegistrationRequestDTO;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeStaff;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.repository.CafeStaffRepository;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import nova.backend.global.util.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static nova.backend.global.error.ErrorCode.ACCESS_DENIED;
import static nova.backend.global.error.ErrorCode.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerCafeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final CafeStaffRepository cafeStaffRepository;
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
    public void updateStampBookDesign(Long ownerId, Long cafeId, String designJson) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (!cafe.getOwner().getUserId().equals(ownerId)) {
            throw new BusinessException(ACCESS_DENIED);
        }

        cafe.setStampBookDesignJson(designJson);
    }


}
