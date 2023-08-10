package my.slack.domain.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.LoginUser;
import my.slack.domain.message.model.MessageCreateRequestDto;
import my.slack.domain.message.model.MessageDto;
import my.slack.domain.user.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    /**
     * @param channelId
     * @return
     */
    @GetMapping
    public BaseResponse<List<MessageDto>> getMessages(@RequestParam Long channelId,@LoginUser User loginUser) {
        return new BaseResponse<>(messageService.getMessagesByChannel(channelId,loginUser));
    }

    @PostMapping
    public BaseResponse<MessageDto> sendMessage(@RequestBody MessageCreateRequestDto messageCreateRequestDto,
                                            @LoginUser User loginUser) {
        MessageDto messageDto = messageService.addMessage(loginUser.getId(), messageCreateRequestDto);
        return new BaseResponse<>(messageDto);
    }


}
