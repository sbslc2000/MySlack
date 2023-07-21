package my.slack.domain.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.response.BaseResponse;
import my.slack.domain.message.model.MessageCreateRequestDto;
import my.slack.domain.message.model.MessageDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    /**
     *
     * @param channelId
     * @return
     */
    @GetMapping
    public BaseResponse<List<MessageDto>> getMessages(@RequestParam Long channelId) {
        return new BaseResponse<>(messageService.getMessagesByChannel(channelId));
    }

    @PostMapping
    public BaseResponse<String> sendMessage(@RequestBody MessageCreateRequestDto messageCreateRequestDto,
                                            @SessionAttribute("userId") String userId) {
        log.info("message create request");
        messageService.addMessage(userId,messageCreateRequestDto);
        return new BaseResponse<>("메시지를 보냈습니다.");
    }


}
