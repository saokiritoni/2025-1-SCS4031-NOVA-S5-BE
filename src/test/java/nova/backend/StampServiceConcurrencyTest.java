package nova.backend;

import nova.backend.domain.cafe.entity.Cafe;
import nova.backend.domain.cafe.entity.CafeRegistrationStatus;
import nova.backend.domain.cafe.entity.CharacterType;
import nova.backend.domain.cafe.repository.CafeRepository;
import nova.backend.domain.stamp.entity.Stamp;
import nova.backend.domain.stamp.repository.StampRepository;
import nova.backend.domain.stamp.service.StampService;
import nova.backend.domain.stampBook.entity.StampBook;
import nova.backend.domain.stampBook.repository.StampBookRepository;
import nova.backend.domain.stampBook.service.UserStampBookService;
import nova.backend.domain.user.entity.Role;
import nova.backend.domain.user.entity.User;
import nova.backend.domain.user.repository.UserRepository;
import nova.backend.global.auth.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class StampServiceConcurrencyTest {

    @Autowired private StampService stampService;
    @Autowired private UserRepository userRepository;
    @Autowired private CafeRepository cafeRepository;
    @Autowired private StampRepository stampRepository;
    @Autowired private UserStampBookService userStampBookService;

    @Test
    void 동시_스탬프적립_중복적립_발생_테스트() throws Exception {
        // 유저 1, 카페 1 가져오기
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("user not found"));
        Cafe cafe = cafeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("cafe not found"));

        // 역할 먼저 체크
        if (!(user.getRole() == Role.STAFF || user.getRole() == Role.OWNER)) {
            throw new RuntimeException("User is not STAFF or OWNER");
        }

        // QR로 유저 식별
        if (user.getQrCodeValue() == null) {
            throw new RuntimeException("User has no qrCodeValue set");
        }

        StampBook book = userStampBookService.getOrCreateValidStampBook(user, cafe);

        CustomUserDetails staffDetails = new CustomUserDetails(user, cafe.getCafeId());

        // 동시에 적립 요청: 클라이언트에서 요청을 2번 보냈을 경우
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            executor.execute(() -> {
                try {
                    stampService.accumulateStamp(staffDetails, user.getQrCodeValue(), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 동시 작업 대기

        // 결과 확인
        int count = stampRepository.countByStampBook_StampBookId(book.getStampBookId());
        System.out.println("동시 적립된 스탬프 수 = " + count);
        assertThat(count).isEqualTo(2); // 통과되면 동시성 문제 발생
    }
}
