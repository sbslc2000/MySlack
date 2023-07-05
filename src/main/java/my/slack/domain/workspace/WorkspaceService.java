package my.slack.domain.workspace;

import lombok.RequiredArgsConstructor;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.ChannelService;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.user.UserService;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static my.slack.api.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public String createWorkspace(String creatorId, WorkspaceCreateRequestDto workspaceCreateRequestDto) {

        User creator = userService.findById(creatorId);

        //workspace 생성
        Workspace workspace = new Workspace(creator, workspaceCreateRequestDto.getName());
        workspaceRepository.save(workspace);

        //요청받은 채널 생성
        String workspaceId = workspace.getId();
        ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(workspaceCreateRequestDto.getChannel(), "", false, List.of(creatorId));
        channelService.createChannel(workspaceId,creatorId,channelCreateRequestDto);

        return workspaceId;
    }

    public List<Workspace> getUserWorkspaces(String userId) {
        return workspaceRepository.findByUserId(userId);
    }




    public Workspace findById(String id) {
        return workspaceRepository.findById(id).orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND,"존재하지 않는 워크스페이스입니다."));
    }

    public void deleteWorkspace(String workspaceId, String deleterId) {
        Workspace workspace = findById(workspaceId);
        User deleter = userService.findById(deleterId);

        if(!workspace.getCreator().equals(deleter)) {
            throw new ClientFaultException(FORBIDDEN,"워크스페이스 삭제 권한이 없습니다.");
        }

        workspaceRepository.deleteById(workspaceId);
    }

    public void enterWorkspace(String userId,String workspaceId) {
        Workspace workspace = findById(workspaceId);
        User user = userService.findById(userId);


        /*
        //허가된 email만이 workspace에 등록될 수 있음
        if(workspace.isPermittedEmail(userEmail)) {
            throw new ClientFaultException(FORBIDDEN,"허가되지 않은 이메일입니다.");
        }

         */

        //Workspace에 User 추가
        if(!workspace.getUsers().contains(user)) {
            workspace.addUser(user);
        }

        //Workspace의 public 한 channel 에 User 추가
        workspace.getChannels().stream()
                .filter(channel -> !channel.isPrivate())
                .forEach(channel -> {
                    channel.addMember(user);
                });


    }

    public List<User> getUsersByWorkspace(String workspaceId, String userId) {
        User user = userService.findById(userId);

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        List<User> users = workspace.getUsers();


        if(!users.contains(user)) {
            throw new ClientFaultException(FORBIDDEN,"워크스페이스에 가입되지 않은 사용자입니다.");
        } else {
            users.remove(user);
            users.add(0,user);
        }

        return users;
    }

    public String createInviteLink(String workspaceId) {
        return "/workspaces/enter/" + workspaceId;

    }
}
