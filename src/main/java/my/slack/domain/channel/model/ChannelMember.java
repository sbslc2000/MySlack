package my.slack.domain.channel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.domain.user.model.User;

@Entity
@Getter
@NoArgsConstructor
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ChannelMember(Channel channel, User user) {
        this.channel = channel;
        this.user = user;
    }
}
