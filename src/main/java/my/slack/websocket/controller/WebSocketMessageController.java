package my.slack.websocket.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import my.slack.domain.user.UserService;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.annotation.MessageMapping;
import my.slack.websocket.annotation.ResponseMessage;
import my.slack.websocket.annotation.WebSocketSessionAttribute;
import my.slack.websocket.model.requestDto.WebSocketMessageTypingStartRequestDto;
import org.springframework.stereotype.Component;

import java.awt.desktop.UserSessionEvent;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final ObjectMapper objectMapper;
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final WorkspaceRepository workspaceRepository;

    @MessageMapping("MESSAGE_TYPING_START")
    @ResponseMessage("MESSAGE_TYPING_START")
    public Object handleTypingStart(@WebSocketSessionAttribute("targetUsers") List<User> targetUsers,
                                    String workspaceId,
                                    String userId,
                                    Long channelId) throws JsonProcessingException {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));



        workspace.getUsers().stream()
                .filter(user -> !user.getId().equals(userId))
                .forEach(user -> targetUsers.add(user));


        User typer = userService.findById(userId);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", objectMapper.writeValueAsString(typer));
        objectNode.put("channelId", channelId);

        return objectNode;
    }

    @MessageMapping("MESSAGE_TYPING_END")
    @ResponseMessage("MESSAGE_TYPING_END")
    public Object handleTypingEnd(@WebSocketSessionAttribute("targetUsers") List<User> targetUsers,
                                    String workspaceId,
                                    String userId,
                                    Long channelId) throws JsonProcessingException {


        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다."));

        workspace.getUsers().stream()
                .filter(user -> !user.getId().equals(userId))
                .forEach(user -> targetUsers.add(user));


        User typer = userService.findById(userId);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", objectMapper.writeValueAsString(typer));
        objectNode.put("channelId", channelId);

        return objectNode;
    }




}
