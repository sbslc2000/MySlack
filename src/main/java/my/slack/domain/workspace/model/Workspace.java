package my.slack.domain.workspace.model;

import lombok.Getter;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
public class Workspace {
    private String id;
    private String name;
    private User creator;
    private List<User> managers = new ArrayList<>();
    private List<User> members = new ArrayList<>();
    private List<Channel> channels = new ArrayList<>();
    private LocalDateTime createdAt;

    private List<String> permittedUserEmail = new ArrayList<>();

    public boolean isPermittedEmail(String email) {
        return permittedUserEmail.contains(email);
    }

    public boolean hasAuthority(User user) {
        return user.equals(creator) || managers.contains(user);
    }


    public Workspace(User creator, String name) {
        this.name = name;
        this.creator = creator;
        this.createdAt = LocalDateTime.now();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public List<User> getUsers() {
        List<User> users = Stream.concat(managers.stream(), members.stream()).distinct()
                .collect(Collectors.toList());
        users.add(creator);
        return users;
    }

    public void addUser(User user) {
        members.add(user);
    }

    public boolean hasUser(String userId) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst().isPresent();
    }

    public void addManager(User user) {
        managers.add(user);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }


}
