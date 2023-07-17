package my.slack.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.annotation.MessageMapping;
import my.slack.websocket.annotation.ResponseMessage;
import my.slack.websocket.annotation.WebSocketSessionAttribute;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketUserController {

    private final ActiveUserService activeUserService;
    private final WorkspaceService workspaceService;
    private final WorkspaceRepository workspaceRepository;
    @MessageMapping("USER_CONNECT")
    @ResponseMessage("REFRESH_DM_USER_LIST")
    public void handleUserConnect(@WebSocketSessionAttribute("userId") String userId,
                                    @WebSocketSessionAttribute("targetUsers") List<User> targetUsers) {


        activeUserService.setConnect(userId);
        List<User> activeUsers = activeUserService.getActiveUsers();


        //접속한 사용자의 워크스페이스 목록을 가져온다.
        List<Workspace> workspaces = workspaceRepository.findByUserId(userId);


        //기존의 사용자의 workspace에 접속한 사용자가 존재한다면, 이는 refresh 대상임

        for (Workspace workspace : workspaces) {
            for (User user : activeUsers) {
                if (workspace.hasUser(user.getId()))
                    targetUsers.add(user);
            }
        }
        targetUsers = targetUsers.stream()
                .distinct()
                .toList();

        //targetUsers.remove(accessUser);

        log.info("REFRESH TARGET : " + targetUsers);
        return;
    }

    @MessageMapping("USER_DISCONNECT")
    @ResponseMessage("REFRESH_DM_USER_LIST")
    public void handleUserDisconnect(@WebSocketSessionAttribute("userId") String userId,
                                       @WebSocketSessionAttribute("targetUsers") List<User> targetUsers) {

        List<User> activeUsers = activeUserService.getActiveUsers();
        activeUserService.setDisconnect(userId);

        List<Workspace> workspaces = workspaceRepository.findByUserId(userId);

        for (Workspace workspace : workspaces) {
            for (User user : activeUsers) {
                if (workspace.hasUser(user.getId()))
                    targetUsers.add(user);
            }
        }

        targetUsers = targetUsers.stream()
                .distinct()
                .toList();


        log.info("REFRESH TARGET : " + targetUsers.toString());
        return;
    }
}
