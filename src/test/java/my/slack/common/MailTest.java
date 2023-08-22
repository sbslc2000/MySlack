package my.slack.common;

import my.slack.BaseIntegratedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class MailTest extends BaseIntegratedTest {


    @Test
    @DisplayName("메일 전송 테스트")
    public void sendMail() {
        mailService.sendMail();
    }
}
