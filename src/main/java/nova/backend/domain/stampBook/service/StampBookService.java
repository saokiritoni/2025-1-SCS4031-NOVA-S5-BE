package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stampBook.dto.response.StampBookResponseDTO;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StampBookService {

    private final StampBookRepository stampBookRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);
        return stampBooks.stream()
                .map(StampBookResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public StampBookResponseDTO createStampBook(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        boolean exists = stampBookRepository.existsByUser_UserIdAndCafe_CafeId(userId, cafeId);
        if (exists) {
            throw new BusinessException(ErrorCode.STAMPBOOK_ALREADY_EXISTS);
        }

        StampBook newStampBook = StampBook.builder()
                .user(user)
                .cafe(cafe)
                .isCompleted(false)
                .rewardClaimed(false)
                .inHome(false)
                .build();

        stampBookRepository.save(newStampBook);
        return StampBookResponseDTO.fromEntity(newStampBook);
    }

}

