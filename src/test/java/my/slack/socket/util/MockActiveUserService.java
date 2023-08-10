package my.slack.socket.util;

import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MockActiveUserService implements ActiveUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getActiveUsers() {
        return List.of(
                userRepository.findById("user1").get(),
                userRepository.findById("user2").get(),
                userRepository.findById("user3").get()
        );
    }

    @Override
    public User setConnect(String userId) {
        return null;
    }

    @Override
    public User setDisconnect(String userId) {
        return null;
    }
}
