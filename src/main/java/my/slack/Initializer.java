package my.slack;

import lombok.RequiredArgsConstructor;
import my.slack.domain.user.UserService;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    @Value("${my.nickname}")
    private String nickname;
    @Value("${my.email}")
    private String email;
    @Value("${my.id}")
    private String id;
    @Value("${my.profileImage}")
    private String profileImage;

    private final UserService userService;
    private final WorkspaceService workspaceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String userId = userService.createUser(id, email, nickname, profileImage);
        workspaceService.createWorkspace(userId,new WorkspaceCreateRequestDto("내 슬랙 프로젝트",null,"기록"));
    }
}
