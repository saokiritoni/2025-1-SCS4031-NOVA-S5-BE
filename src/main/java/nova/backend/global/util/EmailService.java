package nova.backend.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nova.backend.domain.cafe.entity.Cafe;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendCafeRegistrationEmail(Cafe cafe, MultipartFile pdfFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo("dongguk.nova@gmail.com");
            helper.setSubject("[카페 등록 요청] " + cafe.getCafeName());
            helper.setText(
                    """
                    새로운 카페 등록 요청이 접수되었습니다.
                    
                    - 카페명: %s
                    - 지점명: %s
                    - 사업자명: %s
                    - 연락처: %s
                    """.formatted(
                            cafe.getCafeName(),
                            cafe.getBranchName(),
                            cafe.getOwnerName(),
                            cafe.getOwnerPhone()
                    )
            );

            helper.addAttachment("사업자등록증.pdf", pdfFile);

            mailSender.send(message);
            log.info("[EmailService] 사업자 등록증 이메일 전송 완료: {}", cafe.getCafeName());

        } catch (MessagingException e) {
            log.error("[EmailService] 이메일 전송 실패", e);
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }
    }
}

