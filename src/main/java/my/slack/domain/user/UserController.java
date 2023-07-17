package my.slack.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.exception.ClientFaultException;
import my.slack.api.response.BaseResponse;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceService;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static my.slack.api.ErrorCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final WorkspaceService workspaceService;

    @GetMapping("/{userId}/workspaces")
    public BaseResponse<List<WorkspaceDto>> getUserWorkspaces(@PathVariable String userId, @SessionAttribute("userId") String sessionUserId) {

        if(!userId.equals(sessionUserId))
            throw new ClientFaultException(FORBIDDEN,"다른 유저의 워크스페이스를 조회할 수 없습니다.");

        return new BaseResponse<>(workspaceService.getUserWorkspaces(userId));
    }

    @GetMapping("/me/workspaces")
    public BaseResponse<List<WorkspaceDto>> getUserWorkspacesBySession(@SessionAttribute("userId") String userId) {

        List<WorkspaceDto> userWorkspaces = workspaceService.getUserWorkspaces(userId);
        log.info("userWorkspaces = {}",userWorkspaces);

        return new BaseResponse<>(userWorkspaces);
    }

    @GetMapping("/me")
    public BaseResponse<User> getMyUser(@SessionAttribute("userId") String userId) {
        log.info("/api/users/me userId:{}" ,userId);
        User user = userService.findById(userId);
        return new BaseResponse<>(user);
    }




}
