package my.slack.domain.message;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/workspaces/{workspaceId}/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    /**
     * fix me: 추후 사용자에 대한 validation 추가로 필요함
     * @param channelId
     * @return
     */
    @GetMapping
    public BaseResponse<List<MessageDto>> getMessages(@PathVariable Long channelId) {
        return new BaseResponse<>(messageService.getMessagesByChannel(channelId));
    }

    @PostMapping
    public BaseResponse<String> sendMessage(@PathVariable Long channelId, @RequestBody MessageCreateRequestDto messageCreateRequestDto,
                                            @SessionAttribute("userId") String userId) {
        log.info("message create request");
        messageService.addMessage(userId,channelId,messageCreateRequestDto);
        return new BaseResponse<>("메시지를 보냈습니다.");
    }


}
