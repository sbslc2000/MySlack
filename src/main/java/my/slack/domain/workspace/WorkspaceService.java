package my.slack.domain.workspace;

import lombok.RequiredArgsConstructor;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.channel.ChannelService;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.member.Member;
import my.slack.domain.member.MemberRepository;
import my.slack.domain.user.MemoryUserRepository;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static my.slack.api.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final MemberRepository memberRepository;

    public String createWorkspace(String creatorId, WorkspaceCreateRequestDto workspaceCreateRequestDto) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(
                        () -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));

        //workspace 생성
        Workspace workspace = new Workspace(creator, workspaceCreateRequestDto.getName());
        workspaceRepository.save(workspace);

        //요청받은 채널 생성
        String workspaceId = workspace.getId();
        ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(workspaceCreateRequestDto.getChannel(), "", false);
        channelService.createChannel(workspaceId, creatorId, channelCreateRequestDto);

        return workspaceId;
    }

    public List<WorkspaceDto> getUserWorkspaces(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));
        return workspaceRepository.findByUser(user).stream().map(WorkspaceDto::of).toList();
    }


    public WorkspaceDto findById(String id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));
        return WorkspaceDto.of(workspace);
    }

    public void deleteWorkspace(String workspaceId, String deleterId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        User deleter = userRepository.findById(deleterId)
                .orElseThrow(
                        () -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));

        if (!workspace.getCreator()
                .equals(deleter)) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스 삭제 권한이 없습니다.");
        }

        workspaceRepository.delete(workspace);
    }

    public void enterWorkspace(String userId, String workspaceId) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 사용자입니다."));


        /*
        //허가된 email만이 workspace에 등록될 수 있음
        if(workspace.isPermittedEmail(userEmail)) {
            throw new ClientFaultException(FORBIDDEN,"허가되지 않은 이메일입니다.");
        }

         */

        //Workspace에 User 추가
        if (!workspace.hasUser(userId)) {
            addMember(workspace,user);
        } else {
            throw new ClientFaultException(FORBIDDEN, "이미 워크스페이스에 가입된 사용자입니다.");
        }

        /*
        //Workspace의 public 한 channel 에 User 추가
        workspace.getChannels()
                .stream()
                .filter(channel -> !channel.isPrivate())
                .forEach(channel -> {
                    channel.addMember(user);
                });

         */
    }

    private void addMember(Workspace workspace, User user) {
        Member member = new Member(workspace,user);
        memberRepository.save(member);
    }

    public List<User> getUsersByWorkspace(String workspaceId, String userId) {

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ClientFaultException(ENTITY_NOT_FOUND, "존재하지 않는 워크스페이스입니다."));

        if (!workspace.hasUser(userId)) {
            throw new ClientFaultException(FORBIDDEN, "워크스페이스에 가입되지 않은 사용자입니다.");
        } else {
            List<User> users = workspace.getUsers();

            users.sort(Comparator.comparing(user -> user.getId().equals(userId) ? 0 : 1));
            return users;
        }
    }

    public String createInviteLink(String workspaceId) {
        return "/workspaces/enter/" + workspaceId;
    }


}
