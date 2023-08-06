package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.LoginUser;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.channel.model.ChannelMemberCreateRequestDto;
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

    @GetMapping("/{channelId}/members")
    public BaseResponse<List<User>> getMembers(@PathVariable Long channelId,
                                               @RequestParam(defaultValue = "") String searchName,
                                               @LoginUser User loginUser) {
        List<User> members = channelService.findMembers(channelId, searchName, loginUser);
        return new BaseResponse<>(members);
    }

    @PostMapping("/{channelId}/members")
    public BaseResponse<List<User>> addMember(@PathVariable Long channelId, @RequestBody ChannelMemberCreateRequestDto channelMemberCreateRequestDto,
                                              @LoginUser User loginUser) {
        List<User> members = channelService.addMembers(channelId, channelMemberCreateRequestDto, loginUser);
        return new BaseResponse<>(members);
    }




}
