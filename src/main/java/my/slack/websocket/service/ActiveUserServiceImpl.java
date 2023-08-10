package my.slack.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.UserService;
import my.slack.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveUserServiceImpl implements ActiveUserService {

    private final UserService userService;

    private Map<String, User> activeUsers = new ConcurrentHashMap<>();

    @Override
    public List<User> getActiveUsers() {
        //unmodifiableList
        return activeUsers.values().stream().toList();
    }

    @Override
    public User setConnect(String userId) {
        //접속한 유저정보 확인
        User accessUser = userService.findById(userId);

        //ActiveUser Map에 user 추가, User의 active 상태 변경
        activeUsers.put(userId, accessUser);
        accessUser.setStatus(true);
        return accessUser;
    }

    @Override
    public User setDisconnect(String userId) {
        //접속한 유저정보 확인
        User accessUser = userService.findById(userId);

        //ActiveUser Map에 user 추가, User의 active 상태 변경
        activeUsers.remove(userId);
        accessUser.setStatus(false);
        return accessUser;
    }



}
