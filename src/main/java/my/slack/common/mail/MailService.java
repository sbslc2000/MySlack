package my.slack.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.Workspace;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            String[] receiveList = {"sbslc2000@gmail.com"};

            simpleMailMessage.setTo(receiveList);

            // 메일 제목 설정
            simpleMailMessage.setSubject("test_title");

            // 메일 내용 설정
            simpleMailMessage.setText("test_content");

            // 전송
            javaMailSender.send(simpleMailMessage);

        } catch(Exception e) {
            log.error("메일 전송 실패");
        }
    }
}
