package nova.backend.domain.stampBook.service;

import lombok.RequiredArgsConstructor;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.repository.StampRepository;
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
@Transactional
public class StampBookService { //TODO: 이미 존재하는 경우에 500말고 다른 예외로 수정하기

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    public List<StampBookResponseDTO> getStampBooksForUser(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);
        return stampBooks.stream()
                .map(stampBook -> {
                    int current = stampRepository.countByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampBookResponseDTO.fromEntity(stampBook, current);
                })
                .toList();
    }


    @Transactional
    public StampBookResponseDTO createStampBook(Long userId, Long cafeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 1. 기존 스탬프북 조회
        List<StampBook> existing = stampBookRepository.findByUser_UserIdAndCafe_CafeId(userId, cafeId);

        for (StampBook sb : existing) {
            if (!sb.isCompleted()) {
                // 2. 미완료된 스탬프북이 하나라도 있다면 예외
                throw new BusinessException(ErrorCode.STAMPBOOK_ALREADY_EXISTS);
            }
        }

        // 3. 없거나 모두 완료된 경우 새로 생성
        StampBook newStampBook = StampBook.builder()
                .user(user)
                .cafe(cafe)
                .isCompleted(false)
                .rewardClaimed(false)
                .inHome(false)
                .build();

        stampBookRepository.save(newStampBook);
        return StampBookResponseDTO.fromEntity(newStampBook, 0);
    }

    @Transactional
    public StampBook getOrCreateValidStampBook(User user, Cafe cafe) {
        return stampBookRepository
                .findFirstByUser_UserIdAndCafe_CafeIdAndIsCompletedFalse(user.getUserId(), cafe.getCafeId())
                .orElseGet(() -> stampBookRepository.save(
                        StampBook.builder()
                                .user(user)
                                .cafe(cafe)
                                .isCompleted(false)
                                .rewardClaimed(false)
                                .inHome(false)
                                .build()
                ));
    }


}

