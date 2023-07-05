package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import my.slack.api.response.BaseResponse;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.workspace.WorkspaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final WorkspaceService workspaceService;

    @PostMapping
    public BaseResponse<String> addChannel(@PathVariable String workspaceId, @RequestBody ChannelCreateRequestDto channelCreateRequestDto, @SessionAttribute("userId") String userId) {
        Long createdChannelId = channelService.createChannel(workspaceId,userId,channelCreateRequestDto);
        return new BaseResponse<>(createdChannelId.toString());
    }

    @GetMapping
    public BaseResponse<List<ChannelDto>> getChannelsByWorkspace(@PathVariable String workspaceId, @SessionAttribute("userId") String userId) {
        List<ChannelDto> channelDtos = channelService.getChannelsByWorkspaceId(workspaceId,userId);
        return new BaseResponse<>(channelDtos);
    }

    @DeleteMapping("/{channelId}")
    public BaseResponse<String> deleteChannel(@PathVariable Long channelId, @SessionAttribute("userId") String userId) {
        channelService.deleteChannel(channelId,userId);
        return new BaseResponse<>("삭제되었습니다.");
    }


}
