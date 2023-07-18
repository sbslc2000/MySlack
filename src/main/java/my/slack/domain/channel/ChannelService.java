package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.user.UserService;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.MemoryWorkspaceRepository;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.model.Workspace;
import my.slack.websocket.WebSocketMessageSender;
import my.slack.websocket.model.WebSocketMessageRequest;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static my.slack.api.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private final WorkspaceRepository workspaceRepository;
    private final ActiveUserService activeUserService;
    private final WebSocketMessageSender webSocketMessageSender;

    public Long createChannel(String workspaceId, String userId, ChannelCreateRequestDto channelCreateRequestDto) {
        User creator = userService.findById(userId);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(
                        () -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        //channel의 creator 가 workspace의 manager인가?
        //if(!workspace.hasAuthority(creator)) {
        //    throw new ClientFaultException(FORBIDDEN, "워크스페이스의 매니저만 채널을 생성할 수 있습니다.");
        //}


        //if(channelCreateRequestDto.isPrivate()) {
        //initialMembers = channelCreateRequestDto.getInitialMembers()
        //            .stream()
        //            .map(userService::findById)
        //            .collect(Collectors.toList());
        //} else {
        //
        // }


        Channel channel = new Channel(workspace, creator, channelCreateRequestDto.getName(), channelCreateRequestDto.getDescription(),  channelCreateRequestDto.isPrivate());
        channelRepository.save(channel);

        //Workspace 에 채널 추가
        workspace.addChannel(channel);

        notifyChannelChanged(workspace);
        return channel.getId();
    }

    private void notifyChannelChanged(Workspace workspace) {

        List<User> targetUsers = new ArrayList<>();
        List<User> activeUsers = activeUserService.getActiveUsers();

        activeUsers.forEach((user) -> {
            if(workspace.hasUser(user.getId())) {
                targetUsers.add(user);
            }
        });


        WebSocketMessageRequest req = new WebSocketMessageRequest("REFRESH_CHANNEL_LIST",null,targetUsers);
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

        if(!workspace.getManagers().contains(deleter)) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스의 매니저만 채널을 삭제할 수 있습니다.");
        }


        //삭제
        workspace.removeChannel(channel);

        notifyChannelChanged(workspace);
        channelRepository.delete(channel);
    }

    public List<ChannelDto> getChannelsByWorkspaceId(String workspaceId,String userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        if(!workspace.hasUser(userId)) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스에 가입되지 않은 사용자입니다.");
        }

        return workspace.getChannels()
                .stream()
                .map(ChannelDto::of)
                .toList();
    }
}
