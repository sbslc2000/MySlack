package my.slack.domain.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MessageDto {
    private User sender;
    private String content;
    private LocalDateTime createdAt;

    public static MessageDto of(Message message) {
        return new MessageDto(message.getSender(),message.getContent(),message.getCreatedAt());
    }
}
