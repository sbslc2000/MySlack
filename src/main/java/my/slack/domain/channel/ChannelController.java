package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.LoginUser;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final WorkspaceService workspaceService;

    @PostMapping
    public BaseResponse<ChannelDto> addChannel(@PathVariable String workspaceId, @RequestBody ChannelCreateRequestDto channelCreateRequestDto,
                                               @LoginUser User loginUser) {
        ChannelDto channelDto = channelService.createChannel(workspaceId, loginUser.getId(), channelCreateRequestDto);
        return new BaseResponse<>(channelDto);
    }

    @GetMapping
    public BaseResponse<List<ChannelDto>> getChannelsByWorkspace(@PathVariable String workspaceId,
                                                                 @LoginUser User loginUser) {
        List<ChannelDto> channelDtos = channelService.getChannelsByWorkspaceId(workspaceId, loginUser);
        return new BaseResponse<>(channelDtos);
    }

    @DeleteMapping("/{channelId}")
    public BaseResponse<String> deleteChannel(@PathVariable Long channelId, @LoginUser User loginUser) {
        channelService.deleteChannel(channelId, loginUser.getId());
        return new BaseResponse<>("삭제되었습니다.");
    }

    @PostMapping("/{channelId}/members")
    public BaseResponse<String> addMember(@PathVariable Long channelId, @RequestBody List<String> userIds,
                                              @LoginUser User loginUser) {
        channelService.addMembers(channelId, userIds, loginUser);
        return new BaseResponse<>("추가되었습니다.");
    }


}
