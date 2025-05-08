package nova.backend.domain.stamp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.dto.response.RecentStampResponseDTO;
import nova.backend.domain.stamp.dto.response.StaffStampViewResponseDTO;
import nova.backend.domain.stamp.dto.response.StampHistoryResponseDTO;
import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.stampBook.service.StampBookService;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.error.ErrorCode;
import nova.backend.global.error.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final StampBookRepository stampBookRepository;
    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final StampBookService stampBookService;

    // TODO: 로그 메시지 삭제

    public void accumulateStamp(User staff, String targetQrCode, Long cafeId, int count) {
        log.info("[스탬프 적립 시작] staff={}, qrCodeValue={}, cafeId={}, count={}",
                staff.getEmail(), targetQrCode, cafeId, count);

        // 1. 권한 체크
        if (!(staff.getRole() == Role.OWNER || staff.getRole() == Role.STAFF)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 2. 대상 사용자 & 카페 조회
        User targetUser = userRepository.findByQrCodeValue(targetQrCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        // 3. 스탬프 적립
        int stampsToAdd = count;
        while (stampsToAdd > 0) {
            StampBook currentBook = stampBookService.getOrCreateValidStampBook(targetUser, cafe);

            int currentCount = stampRepository.countByStampBook_StampBookId(currentBook.getStampBookId());
            int max = cafe.getMaxStampCount();
            int space = max - currentCount;

            int toAddNow = Math.min(space, stampsToAdd);
            // 스탬프북이 꽉 찬 상태에서 추가 요청할 경우 처리
            if (toAddNow <= 0) { throw new BusinessException(ErrorCode.BAD_REQUEST); }
            for (int i = 0; i < toAddNow; i++) { stampRepository.save(Stamp.builder().stampBook(currentBook).build()); }
            if (currentCount + toAddNow == max) { currentBook.markAsCompleted(); }
            stampsToAdd -= toAddNow;
        }

        log.info("[스탬프 적립 종료] staff={}, targetUser={}, cafeId={}, count={}",
                staff.getEmail(), targetUser.getEmail(), cafeId, count);
    }



    public List<StampHistoryResponseDTO> getStampHistory(Long userId) {
        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(userId);

        return stampBooks.stream()
                .map(stampBook -> {
                    List<Stamp> stamps = stampRepository.findByStampBook_StampBookId(stampBook.getStampBookId());
                    return StampHistoryResponseDTO.fromEntity(stampBook, stamps);
                })
                .toList();
    }


//    @Transactional(readOnly = true)
//    public StaffStampViewResponseDTO getStampHistoryForStaffView(Long targetUserId, Long staffUserId) {
//        User staffUser = userRepository.findById(staffUserId)
//                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
//
//        if (!(staffUser.getRole() == Role.OWNER || staffUser.getRole() == Role.STAFF)) {
//            throw new BusinessException(ErrorCode.FORBIDDEN);
//        }
//
//        Cafe staffCafe = staffUser.getCafe();
//        if (staffCafe == null) {
//            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
//        }
//
//        // 1️⃣ 고객 이력
//        List<StampBook> stampBooks = stampBookRepository.findByUser_UserId(targetUserId);
//        List<StampHistoryResponseDTO> history = stampBooks.stream()
//                .map(stampBook -> {
//                    List<Stamp> stamps = stampRepository.findByStampBook_StampBookId(stampBook.getStampBookId());
//                    return StampHistoryResponseDTO.fromEntity(stampBook, stamps);
//                })
//                .toList();
//
//        // 2️⃣ 최근 적립 3개
//        List<Stamp> recentStamps = stampRepository.findTop3ByStampBook_Cafe_CafeIdOrderByCreatedAtDesc(staffCafe.getCafeId());
//        List<RecentStampResponseDTO> recentStampDtos = recentStamps.stream()
//                .map(RecentStampResponseDTO::fromEntity)
//                .toList();
//
//        return new StaffStampViewResponseDTO(history, recentStampDtos);
//    }

}
