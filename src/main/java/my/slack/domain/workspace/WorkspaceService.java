package my.slack.domain.workspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.exception.ClientFaultException;
import my.slack.common.login.LoginUser;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.channel.ChannelService;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.member.Member;
import my.slack.domain.member.MemberRepository;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.exception.UserNotFound;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.exception.WorkspaceNotFound;
import my.slack.domain.workspace.exception.WorkspaceNotJoined;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static my.slack.api.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChannelService channelService;
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;

    public WorkspaceDto createWorkspace(String creatorId, WorkspaceCreateRequestDto workspaceCreateRequestDto) {

        User creator = findUser(creatorId);

        //workspace 생성
        Workspace workspace = new Workspace(creator, workspaceCreateRequestDto.getName());
        Workspace savedWorkspace = workspaceRepository.save(workspace);

        //요청받은 채널 생성
        String workspaceId = workspace.getId();
        ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(workspaceId,workspaceCreateRequestDto.getChannel(), "", false);
        channelService.createChannel(channelCreateRequestDto,creator);

        return WorkspaceDto.of(savedWorkspace);
    }

    public List<WorkspaceDto> getUserWorkspaces(String userId) {
        User user = findUser(userId);

        return workspaceRepository.findByUser(user)
                .stream()
                .map(WorkspaceDto::of)
                .toList();
    }


    public WorkspaceDto findById(String id) {
        Workspace workspace = findWorkspace(id);
        return WorkspaceDto.of(workspace);
    }

    public void deleteWorkspace(String workspaceId, String deleterId) {
        Workspace workspace = findWorkspace(workspaceId);
        User deleter = findUser(deleterId);

        if (!workspace.getCreator().equals(deleter)) {
            throw new ClientFaultException(PERMISSION_DENIED, "워크스페이스 삭제 권한이 없습니다.");
        }

        workspaceRepository.delete(workspace);
    }

    public void enterWorkspace(String userId, String workspaceId) {

        Workspace workspace = findWorkspace(workspaceId);
        User user = findUser(userId);

        /*
        //허가된 email만이 workspace에 등록될 수 있음
        if(workspace.isPermittedEmail(userEmail)) {
            throw new ClientFaultException(FORBIDDEN,"허가되지 않은 이메일입니다.");
        }

         */

        //Workspace에 User 추가
        if (!workspace.hasUser(userId)) {
            addMember(workspace, user);
        } else {
            throw new ClientFaultException(WORKSPACE_ALREADY_JOINED);
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
        Member member = new Member(workspace, user);
        memberRepository.save(member);
        workspace.getMembers().add(member);
    }

    public List<User> getUsersByWorkspace(String workspaceId, String search, User loginUser) {

        Workspace workspace = findWorkspace(workspaceId);

        checkUserIsInWorkspace(workspace, loginUser);

        List<User> users = workspace.getUsers()
                .stream()
                .filter(user -> user.getNickname()
                        .contains(search))
                .sorted(Comparator.comparing(user -> user.getId()
                        .equals(loginUser.getId()) ? 0 : 1))
                .toList();

        log.debug("users: {}", users);
        return users;
    }

    public String createInviteLink(String workspaceId) {
        return "/workspaces/enter/" + workspaceId;
    }

    public List<User> searchWorkspaceUsers(String workspaceId, String searchName, @LoginUser User loginUser) {
        Workspace workspace = findWorkspace(workspaceId);

        checkUserIsInWorkspace(workspace, loginUser);

        return workspace.getUsers()
                .stream()
                .filter(user -> user.getNickname().contains(searchName))
                .toList();
    }

    //private Method
    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
    }

    private Workspace findWorkspace(String workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFound::new);
    }

    private void checkUserIsInWorkspace(Workspace workspace, User user) {
        if (!workspace.hasUser(user)) {
            throw new WorkspaceNotJoined();
        }
    }




}
