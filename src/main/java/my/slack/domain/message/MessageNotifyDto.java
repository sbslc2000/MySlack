package my.slack.domain.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageNotifyDto {
    private Long channelId;
}
