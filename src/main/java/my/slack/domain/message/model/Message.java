package my.slack.domain.message.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.slack.common.model.BaseTimeEntity;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name="channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    private String content;

    public Message(User sender,Channel channel ,String content) {
        this.sender = sender;
        this.channel = channel;
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
