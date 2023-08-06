package my.slack.domain.workspace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.LoginUser;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public BaseResponse<List<User>> getMembersByWorkspace(@PathVariable String workspaceId,
                                                          @LoginUser User loginUser,
                                                          @RequestParam(defaultValue = "") String search) {

        return new BaseResponse<>(workspaceService.getUsersByWorkspace(workspaceId, search, loginUser));
    }

    @PostMapping
    public BaseResponse<WorkspaceDto> createWorkspace(@LoginUser User loginUser, @RequestBody WorkspaceCreateRequestDto workspaceCreateRequestDto) {
        WorkspaceDto workspaceDto = workspaceService.createWorkspace(loginUser.getId(), workspaceCreateRequestDto);
        return new BaseResponse<>(workspaceDto);
    }

    @DeleteMapping("/{workspaceId}")
    public BaseResponse<String> deleteWorkspace(@LoginUser User loginUser, @PathVariable String workspaceId) {
        workspaceService.deleteWorkspace(workspaceId, loginUser.getId());
        return new BaseResponse<>("삭제되었습니다.");
    }

    @PostMapping("/{workspaceId}/users")
    public BaseResponse<String> addUserToWorkspace(@LoginUser User loginUser, @PathVariable String workspaceId) {
        log.info("/api/workspaces/{}/users", workspaceId);
        workspaceService.enterWorkspace(loginUser.getId(), workspaceId);
        return new BaseResponse<>("추가되었습니다.");
    }


}
