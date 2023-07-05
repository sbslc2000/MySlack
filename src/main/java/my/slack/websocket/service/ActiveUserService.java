package my.slack.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.UserService;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.annotation.MessageMapping;
import my.slack.websocket.annotation.WebSocketSessionAttribute;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@RequiredArgsConstructor
public class ActiveUserService {

    private final UserService userService;

    private Map<String, User> activeUsers = new ConcurrentHashMap<>();

    public List<User> getActiveUsers() {
        return Collections.unmodifiableList(activeUsers.values().stream().toList());
    }

    public void setConnect(String userId) {
        //접속한 유저정보 확인
        User accessUser = userService.findById(userId);

        //ActiveUser Map에 user 추가, User의 active 상태 변경
        activeUsers.put(userId, accessUser);
        accessUser.setActive(true);
    }

    public void setDisconnect(String userId) {
        //접속한 유저정보 확인
        User accessUser = userService.findById(userId);

        //ActiveUser Map에 user 추가, User의 active 상태 변경
        activeUsers.remove(userId);
        accessUser.setActive(false);
    }



}
