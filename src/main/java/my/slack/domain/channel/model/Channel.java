package my.slack.domain.channel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.slack.common.model.BaseTimeEntity;
import my.slack.domain.message.model.Message;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Channel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @Setter
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "channel",cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    private boolean isPrivate;


    public Channel(Workspace workspace,User creator,String name,String description, boolean isPrivate) {
        this.workspace = workspace;
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
    }

    public void addMessage(Message message) {
        messages.add(message);
        message.setChannel(this);
    }

    public void setId(Long id) {
        this.id = id;
    }
}
