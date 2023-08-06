package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.model.*;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.UserService;
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

import static my.slack.api.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ActiveUserService activeUserService;
    private final WebSocketMessageSender webSocketMessageSender;
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelDto createChannel(String workspaceId, String userId, ChannelCreateRequestDto channelCreateRequestDto) {
        User creator = userService.findById(userId);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(
                        () -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        //channel의 creator 가 workspace의 manager인가?
        //if(!workspace.hasAuthority(creator)) {
        //    throw new ClientFaultException(FORBIDDEN, "워크스페이스의 매니저만 채널을 생성할 수 있습니다.");
        //}


        Channel channel = new Channel(workspace, creator, channelCreateRequestDto.getName(), channelCreateRequestDto.getDescription(), channelCreateRequestDto.isPrivate());

        if (channelCreateRequestDto.isPrivate()) {
            addMemberToChannel(channel, creator);
        }

        channelRepository.save(channel);

        //Workspace 에 채널 추가
        workspace.addChannel(channel);

        notifyChannelChanged(workspace);
        return ChannelDto.of(channel);
    }

    private void notifyChannelChanged(Workspace workspace) {

        List<User> targetUsers = new ArrayList<>();
        List<User> activeUsers = activeUserService.getActiveUsers();

        activeUsers.forEach((user) -> {
            if (workspace.hasUser(user.getId())) {
                targetUsers.add(user);
            }
        });


        WebSocketMessageRequest req = new WebSocketMessageRequest("REFRESH_CHANNEL_LIST", null, targetUsers);
        webSocketMessageSender.sendMessage(req);
    }

    public void deleteChannel(Long channelId, String deleterId) {
        User deleter = userService.findById(deleterId);

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        /*
        Workspace workspace = workspaceRepository.findById(channel.getWorkspaceId())
                .orElseThrow(() -> new ClientFaultException(DATA_INTEGRITY_FAILURE, "존재하지 않는 워크스페이스입니다. 데이터 무결성이 깨졌습니다."));

         */

        Workspace workspace = channel.getWorkspace();

        if (!workspace.getManagers()
                .contains(deleter)) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스의 매니저만 채널을 삭제할 수 있습니다.");
        }

        //삭제
        workspace.removeChannel(channel);

        notifyChannelChanged(workspace);
        channelRepository.delete(channel);
    }

    public List<ChannelDto> getChannelsByWorkspaceId(String workspaceId, User user) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        if (!workspace.hasUser(user.getId())) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스에 가입되지 않은 사용자입니다.");
        }

        return workspace.getChannels()
                .stream()
                .filter(channel -> channel.isPublic() || channel.hasMember(user))
                .map(ChannelDto::of)
                .toList();
    }

    public void changeToPublic(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        channel.changeToPublic();
        notifyChannelChanged(channel.getWorkspace());
    }

    public List<User> addMembers(Long channelId, ChannelMemberCreateRequestDto channelMemberCreateRequestDto, User loginUser) {

        String userId = channelMemberCreateRequestDto.getUserId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        if (!channel.hasMember(loginUser) && !channel.getWorkspace()
                .hasAuthority(loginUser)) {
            throw new ClientFaultException(FORBIDDEN, "채널의 사용자를 추가할 권한이 없습니다.");
        }

        if (!channel.getWorkspace()
                .hasUser(userId)) {
            throw new ClientFaultException(ENTITY_NOT_FOUND, "사용자가 워크스페이스에 참여하고 있지 않습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));

        addMemberToChannel(channel, user);

        notifyChannelChanged(channel.getWorkspace());
        return channel.getMembers();
    }

    private void addMemberToChannel(Channel channel, User user) {
        //이미 등록되어있다면?
        if (channel.hasMember(user)) {
            throw new ClientFaultException(INVALID_REQUEST, "이미 채널에 등록되어있습니다.");
        }

        ChannelMember channelMember = new ChannelMember(channel, user);
        channelMemberRepository.save(channelMember);

        channel.addMember(channelMember);
    }

    public List<User> findMembers(Long channelId, String username, User loginUser) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        if(!channel.hasMember(loginUser) && !channel.getWorkspace().hasAuthority(loginUser)) {
            throw new ClientFaultException(FORBIDDEN, "채널의 사용자를 조회할 권한이 없습니다.");
        }

        return channel.getMembers().stream().filter(user -> user.getNickname().contains(username)).toList();
    }
}
