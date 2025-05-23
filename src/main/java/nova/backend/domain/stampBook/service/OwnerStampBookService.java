package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.cafe.dto.request.StampBookCreateRequestDTO;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nova.backend.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerStampBookService {

    private final StampBookRepository stampBookRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    public StampBookResponseDTO createStampBook(Long ownerId, StampBookCreateRequestDTO request) {

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(request.cafeId())
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        StampBook newStampBook = request.toEntity(user, cafe);
        stampBookRepository.save(newStampBook);
        return StampBookResponseDTO.fromEntity(newStampBook, 0);
    }

    public void customizeStampBook(Long ownerId, Long stampBookId, String customData) {
        StampBook stampBook = stampBookRepository.findById(stampBookId)
                .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        // owner 권한 검증 필요 (카페의 주인이 ownerId인지)
        if (!stampBook.getCafe().getOwner().getUserId().equals(ownerId)) {
            throw new BusinessException(ACCESS_DENIED);
        }

    }
}

