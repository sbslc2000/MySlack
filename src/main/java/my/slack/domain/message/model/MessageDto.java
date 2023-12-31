package my.slack.domain.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MessageDto {
    private Long id;
    private User sender;
    private String content;
    private LocalDateTime createdAt;

    public static MessageDto of(Message message) {
        return new MessageDto(message.getId(), message.getSender(), message.getContent(), message.getCreatedAt());
    }
}
