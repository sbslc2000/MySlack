package my.slack.domain.workspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.response.BaseResponse;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @GetMapping("/{workspaceId}")
    public BaseResponse<WorkspaceDto> getWorkspace(@PathVariable String workspaceId) {
        log.info("/api/workspaces/{}", workspaceId);
        WorkspaceDto workspaceDto = workspaceService.findById(workspaceId);
        return new BaseResponse<>(workspaceDto);
    }

    @GetMapping("/{workspaceId}/invite-link")
    public BaseResponse<String> getInviteLink(@PathVariable String workspaceId) {
        return new BaseResponse<>(workspaceService.createInviteLink(workspaceId));
    }

    @GetMapping("/{workspaceId}/users")
    public BaseResponse<List<User>> getMembersByWorkspace(@PathVariable String workspaceId,@SessionAttribute(name="userId") String userId) {
        return new BaseResponse<>(workspaceService.getUsersByWorkspace(workspaceId, userId));
    }

    @PostMapping
    public BaseResponse<WorkspaceDto> createWorkspace(@SessionAttribute(name="userId") String creatorId,@RequestBody WorkspaceCreateRequestDto workspaceCreateRequestDto) {
        WorkspaceDto workspaceDto = workspaceService.createWorkspace(creatorId, workspaceCreateRequestDto);
        return new BaseResponse<>(workspaceDto);
    }

    @DeleteMapping("/{workspaceId}")
    public BaseResponse<String> deleteWorkspace(@SessionAttribute(name="userId") String deleterId, @PathVariable String workspaceId) {
        workspaceService.deleteWorkspace(workspaceId, deleterId);
        return new BaseResponse<>("삭제되었습니다.");
    }

    @PostMapping("/{workspaceId}/users")
    public BaseResponse<String> addUserToWorkspace(@SessionAttribute("userId") String userId, @PathVariable String workspaceId) {
        log.info("/api/workspaces/{}/users", workspaceId);
        workspaceService.enterWorkspace(userId, workspaceId);
        return new BaseResponse<>("추가되었습니다.");
    }


}
