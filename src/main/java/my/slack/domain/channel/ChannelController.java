package my.slack.domain.channel;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.LoginUser;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import my.slack.domain.channel.model.ChannelDto;
import my.slack.domain.channel.model.ChannelMemberCreateRequestDto;
import my.slack.domain.channel.model.ChannelUpdateRequestDto;
import my.slack.domain.message.MessageService;
import my.slack.domain.message.model.MessageDto;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.WorkspaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final MessageService messageService;

    @PostMapping
    public BaseResponse<ChannelDto> addChannel(@RequestBody ChannelCreateRequestDto channelCreateRequestDto,
                                               @LoginUser User loginUser) {
        ChannelDto channelDto = channelService.createChannel(channelCreateRequestDto, loginUser);
        return new BaseResponse<>(channelDto);
    }

    @PatchMapping ("/{channelId}")
    public BaseResponse<ChannelDto> changeChannel(@PathVariable Long channelId,
                                                  @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto,
                                                  @LoginUser User loginUser) {

        ChannelDto channelDto = channelService.updateChannel(channelId, channelUpdateRequestDto, loginUser);
        return new BaseResponse<>(channelDto);
    }

    @DeleteMapping("/{channelId}")
    public BaseResponse<String> deleteChannel(@PathVariable Long channelId, @LoginUser User loginUser) {
        channelService.deleteChannel(channelId, loginUser);
        return new BaseResponse<>("삭제되었습니다.");
    }

    @GetMapping("/{channelId}/members")
    public BaseResponse<List<User>> getMembers(@PathVariable Long channelId,
                                               @RequestParam(defaultValue = "") String searchName,
                                               @LoginUser User loginUser) {
        List<User> members = channelService.findMembers(channelId, searchName, loginUser);
        return new BaseResponse<>(members);
    }

    @GetMapping("/{channelId}/messages")
    public BaseResponse<List<MessageDto>> getMessagesByChannel(@PathVariable Long channelId,
                                                         @LoginUser User loginUser) {
        List<MessageDto> messageDtos = messageService.getMessagesByChannel(channelId, loginUser);
        return new BaseResponse<>(messageDtos);
    }

    @PostMapping("/{channelId}/members")
    public BaseResponse<List<User>> addMember(@PathVariable Long channelId, @RequestBody ChannelMemberCreateRequestDto channelMemberCreateRequestDto,
                                              @LoginUser User loginUser) {
        List<User> members = channelService.addMembers(channelId, channelMemberCreateRequestDto, loginUser);
        return new BaseResponse<>(members);
    }


}
