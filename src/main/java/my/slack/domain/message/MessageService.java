package my.slack.domain.message;


import lombok.RequiredArgsConstructor;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.channel.exception.ChannelNotFound;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.message.model.Message;
import my.slack.domain.message.model.MessageCreateRequestDto;
import my.slack.domain.message.model.MessageDto;
import my.slack.domain.message.model.MessageNotifyDto;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.exception.UserNotFound;
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

        Channel channel = findChannel(messageCreateRequestDto.getChannelId());

        Workspace workspace = channel.getWorkspace();

        User user = findUser(userId);

        if (!workspace.hasUser(user)) {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "메시지를 보낼 수 있는 사용자가 아닙니다.");
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

    public List<MessageDto> getMessagesByChannel(Long channelId, User loginUser) {

        Channel channel = findChannel(channelId);

        if(hasMessageViewAuthority(channel,loginUser)) {
            return channel.getMessages()
                    .stream()
                    .map(MessageDto::of)
                    .toList();
        } else {
            throw new ClientFaultException(ErrorCode.PERMISSION_DENIED, "메시지를 볼 수 있는 사용자가 아닙니다.");
        }
    }

    //loginUser가 workspace에 속해있으면서 channel 이 public이거나, private이면서 채널에 멤버로 포함되어있다면
    private boolean hasMessageViewAuthority(Channel channel, User loginUser) {
        return channel.isPublic() && channel.getWorkspace().hasUser(loginUser) ||
        channel.isPrivate() && channel.hasMember(loginUser);
    }

    //private method
    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
    }

    private Channel findChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(ChannelNotFound::new);
    }

}
