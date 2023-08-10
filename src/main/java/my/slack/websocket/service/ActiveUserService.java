package my.slack.websocket.service;

import my.slack.domain.user.model.User;

import java.util.List;

public interface ActiveUserService {
    List<User> getActiveUsers();

    User setConnect(String userId);

    User setDisconnect(String userId);
}
