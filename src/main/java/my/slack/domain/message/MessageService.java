package my.slack.domain.message;


import lombok.RequiredArgsConstructor;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.message.model.Message;
import my.slack.domain.message.model.MessageCreateRequestDto;
import my.slack.domain.message.model.MessageDto;
import my.slack.domain.message.model.MessageNotifyDto;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.WebSocketMessageSender;
import my.slack.websocket.model.WebSocketMessageRequest;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ActiveUserService activeUserService;
    private final WebSocketMessageSender webSocketMessageSender;

    /**
     * @param userId
     * @param messageCreateRequestDto
     * @return fix me
     * 데이터 가져오는 부분 JPA 적용으로 인해 바뀔 필요 있음
     */
    public MessageDto addMessage(String userId, MessageCreateRequestDto messageCreateRequestDto) {


        Channel channel = channelRepository.findById(messageCreateRequestDto.getChannelId())
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        Workspace workspace = channel.getWorkspace();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));

        if (!workspace.hasUser(user.getId())) {
            throw new ClientFaultException(ErrorCode.FORBIDDEN, "메시지를 보낼 수 있는 사용자가 아닙니다.");
        }

        Message message = new Message(user, channel, messageCreateRequestDto.getContent());
        Message createdMessage = messageRepository.save(message);

        //channel.addMessage(message);


        notifyMessageCreated(workspace, channel.getId());

        return MessageDto.of(createdMessage);
    }

    private void notifyMessageCreated(Workspace workspace, Long channelId) {

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

    public List<MessageDto> getMessagesByChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(
                        () -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));
        return channel.getMessages()
                .stream()
                .map(MessageDto::of)
                .toList();
    }

}
