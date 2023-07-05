package my.slack.domain.message;

import lombok.Getter;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Message {
    private Long id;
    private Long channelId;
    private User sender;
    private String content;
    private LocalDateTime createdAt;

    public Message(User sender,Long channelId ,String content) {
        this.sender = sender;
        this.channelId = channelId;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

}
