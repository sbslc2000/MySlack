package my.slack.domain;

import lombok.RequiredArgsConstructor;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.UserRepository;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.domain.workspace.model.Workspace;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceValidator {

    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public boolean isChannelInWorkspace(String workspaceId, Long channelId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 채널입니다."));

        return workspace.getChannels().contains(channel);
    }

    public boolean isUserInWorkspace(String workspaceId, String userId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ErrorCode.ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        return workspace.hasUser(userId);
    }





}
