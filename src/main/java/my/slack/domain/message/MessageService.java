package my.slack.domain.message;


import lombok.RequiredArgsConstructor;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.api.exception.ServerFaultException;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.WebSocketMessageSender;
import my.slack.websocket.model.WebSocketMessageRequest;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ActiveUserService activeUserService;
    private final WebSocketMessageSender webSocketMessageSender;

    public Long addMessage(String userId, Long channelId, MessageCreateRequestDto messageCreateRequestDto) {


        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        Workspace workspace = workspaceRepository.findById(channel.getWorkspaceId())
                .orElseThrow(() -> new ServerFaultException(ErrorCode.DATA_INTEGRITY_FAILURE, "존재하지 않는 워크스페이스입니다. 데이터 무결성이 깨졌습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));

        Message message = new Message(user, channelId, messageCreateRequestDto.getContent());



        channel.addMessage(message);
        Long messageId = messageRepository.save(message);

        notifyMessageCreated(workspace,channelId);
        return messageId;
    }

    private void notifyMessageCreated(Workspace workspace,Long channelId) {

        List<User> targetUsers = new ArrayList<>();
        List<User> activeUsers = activeUserService.getActiveUsers();

        activeUsers.forEach((user) -> {
            if(workspace.hasUser(user.getId())) {
                targetUsers.add(user);
            }
        });

        MessageNotifyDto dto = new MessageNotifyDto(channelId);
        WebSocketMessageRequest req = new WebSocketMessageRequest("REFRESH_MESSAGES",dto,targetUsers);
        webSocketMessageSender.sendMessage(req);
    }

    public List<MessageDto> getMessagesByChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(
                        () -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 채널입니다.")
                );
        return channel.getMessages().stream().map(MessageDto::of).toList();
    }

}
