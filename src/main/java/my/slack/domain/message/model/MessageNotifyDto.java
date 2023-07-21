package my.slack.domain.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MessageNotifyDto {
    private Long channelId;
}
