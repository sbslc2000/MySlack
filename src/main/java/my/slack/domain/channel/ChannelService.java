package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.exception.ClientFaultException;
import my.slack.common.socket.WebSocketNotifyService;
import my.slack.domain.channel.exception.ChannelNotFound;
import my.slack.domain.channel.model.*;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.UserService;
import my.slack.domain.user.exception.UserNotFound;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.exception.WorkspaceNotFound;
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
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final WebSocketNotifyService webSocketNotifyService;

    public ChannelDto createChannel(ChannelCreateRequestDto channelCreateRequestDto,User loginUser) {
        User creator = loginUser;
        Workspace workspace = findWorkspace(channelCreateRequestDto.getWorkspaceId());

        //channel의 creator 가 workspace의 manager인가?
        //if(!workspace.hasAuthority(creator)) {
        //    throw new ClientFaultException(FORBIDDEN, "워크스페이스의 매니저만 채널을 생성할 수 있습니다.");
        //}

        Channel channel = new Channel(workspace, creator, channelCreateRequestDto.getName(), channelCreateRequestDto.getDescription(), channelCreateRequestDto.isPrivate());
        channelRepository.save(channel);

        if (channelCreateRequestDto.isPrivate()) {
            addMemberToChannel(channel, creator);
        }

        //Workspace 에 채널 추가
        workspace.addChannel(channel);

        webSocketNotifyService.notifyChannelChanged(workspace);
        return ChannelDto.of(channel);
    }

    public void deleteChannel(Long channelId, User deleter) {
        Channel channel = findChannel(channelId);
        Workspace workspace = channel.getWorkspace();

        if (!workspace.isAdmin(deleter)) {
            throw new ClientFaultException(PERMISSION_DENIED, "워크스페이스의 매니저만 채널을 삭제할 수 있습니다.");
        }

        //삭제
        workspace.removeChannel(channel);

        webSocketNotifyService.notifyChannelChanged(workspace);
        channelRepository.delete(channel);
    }

    public List<ChannelDto> getChannelsByWorkspaceId(String workspaceId, User user) {
        Workspace workspace = findWorkspace(workspaceId);

        if (!workspace.hasUser(user)) {
            throw new ClientFaultException(WORKSPACE_NOT_JOINED, "워크스페이스에 가입되지 않은 사용자입니다.");
        }

        return workspace.getChannels()
                .stream()
                .filter(channel -> channel.isPublic() || channel.hasMember(user))
                .map(ChannelDto::of)
                .toList();
    }

    public void changeToPublic(Long channelId) {
        Channel channel = findChannel(channelId);

        channel.changeToPublic();
        webSocketNotifyService.notifyChannelChanged(channel.getWorkspace());
    }

    public ChannelDto updateChannel(Long channelId,ChannelUpdateRequestDto channelUpdateRequestDto, User loginUser) {
        Channel channel = findChannel(channelId);
        if(!channel.getCreator().equals(loginUser)) {
            throw new ClientFaultException(PERMISSION_DENIED, "채널의 생성자만 채널을 수정할 수 있습니다.");
        }

        if(channelUpdateRequestDto.getName() != null) {
            channel.setName(channelUpdateRequestDto.getName());
        }

        webSocketNotifyService.notifyChannelChanged(channel.getWorkspace());
        return ChannelDto.of(channel);
    }

    public List<User> addMembers(Long channelId, ChannelMemberCreateRequestDto channelMemberCreateRequestDto, User loginUser) {

        String userId = channelMemberCreateRequestDto.getUserId();

        Channel channel = findChannel(channelId);

        //채널에 포함되어있어야만 멤버 추가 가능
        if (!channel.hasMember(loginUser)) {
            throw new ClientFaultException(FORBIDDEN, "채널의 사용자를 추가할 권한이 없습니다.");
        }

        User user = findUser(userId);

        addMemberToChannel(channel, user);

        webSocketNotifyService.notifyChannelChanged(channel.getWorkspace());
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
        Channel channel = findChannel(channelId);

        if(!channel.hasMember(loginUser) && !channel.getWorkspace().isAdmin(loginUser)) {
            throw new ClientFaultException(FORBIDDEN, "채널의 사용자를 조회할 권한이 없습니다.");
        }

        return channel.getMembers().stream().filter(user -> user.getNickname().contains(username)).toList();
    }


    //private common method
    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
    }

    private Workspace findWorkspace(String workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFound::new);
    }

    private Channel findChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(ChannelNotFound::new);
    }
}
