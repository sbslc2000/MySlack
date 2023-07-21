package my.slack.domain.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {
    private Long channelId;
    private String content;
}
