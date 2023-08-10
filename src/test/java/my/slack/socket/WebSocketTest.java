package my.slack.socket;

import my.slack.BaseIntegratedTest;
import my.slack.domain.message.MessageService;
import my.slack.domain.workspace.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class WebSocketTest extends BaseIntegratedTest {

    @Autowired private ApplicationContext ac;

    private MessageService mockMessageService = new MessageService(
            workspaceRepository,
            channelRepository,
            messageRepository,
            userRepository,
            null,
            null
    );

    @Test
    @DisplayName("MessageRefresh Test")
    void messageRefreshTest() {

    }
}
