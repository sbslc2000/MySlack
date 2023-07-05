package my.slack.domain.channel.model;

import lombok.Getter;
import my.slack.domain.message.Message;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Channel {
    private Long id;
    private String name;
    private String description;
    private User creator;
    private String workspaceId;
    private List<User> members;
    private LocalDateTime createdAt;
    private List<Message> messages = new ArrayList<>();
    private boolean isPrivate;

    public Channel(String workspaceId,User creator,String name,String description, List<User> members, boolean isPrivate) {
        this.workspaceId = workspaceId;
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.members = members;
        this.createdAt = LocalDateTime.now();
        this.isPrivate = isPrivate;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addMember(User user) {
        members.add(user);
    }
}
