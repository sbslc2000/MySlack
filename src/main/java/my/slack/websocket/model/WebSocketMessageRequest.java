package my.slack.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import my.slack.domain.user.model.User;

import java.util.List;

@AllArgsConstructor
@Getter
public class WebSocketMessageRequest {
    private String message;
    private Object body;
    private List<User> refreshTarget;
}
