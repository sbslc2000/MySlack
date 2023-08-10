package my.slack.common.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.message.model.MessageNotifyDto;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.WebSocketMessageSender;
import my.slack.websocket.model.WebSocketMessageRequest;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebSocketNotifyService {

    private final WebSocketMessageSender webSocketMessageSender;
    private final ActiveUserService activeUserService;

    public void notifyChannelChanged(Workspace workspace) {

        List<User> targetUsers = new ArrayList<>();
        List<User> activeUsers = activeUserService.getActiveUsers();

        activeUsers.forEach((user) -> {
            if (workspace.hasUser(user.getId())) {
                targetUsers.add(user);
            }
        });

        WebSocketMessageRequest req = new WebSocketMessageRequest("REFRESH_CHANNEL_LIST", null, targetUsers);
        webSocketMessageSender.sendMessage(req);
    }

    public void notifyMessageCreated(Workspace workspace, Long channelId) {

        List<User> targetUsers = new ArrayList<>();
        List<User> activeUsers = activeUserService.getActiveUsers();

        activeUsers.forEach((user) -> {
            if (workspace.hasUser(user.getId())) {
                targetUsers.add(user);
            }
        });

        MessageNotifyDto dto = new MessageNotifyDto(channelId);
        WebSocketMessageRequest req = new WebSocketMessageRequest("REFRESH_MESSAGES", dto, targetUsers);
        webSocketMessageSender.sendMessage(req);
    }
}
