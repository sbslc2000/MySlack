package my.slack.domain.workspace.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.common.model.BaseTimeEntity;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.member.Manager;
import my.slack.domain.member.Member;
import my.slack.domain.user.model.User;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Getter
@Entity
@NoArgsConstructor
public class Workspace extends BaseTimeEntity {
    @Id
    private String id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "workspace")
    private List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Channel> channels = new ArrayList<>();

    public boolean hasAuthority(User user) {
        return user.equals(creator) || managers.contains(user);
    }


    public Workspace(User creator, String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.creator = creator;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.setWorkspace(this);
    }

    public List<User> getUsers() {
        List<User> users = Stream.concat(managers.stream().map(Manager::getUser), members.stream().map(Member::getUser)).distinct()
                .collect(Collectors.toList());
        users.add(creator);
        return users;
    }

    public void addUser(User user) {
        Member member = new Member(this, user);
        members.add(member);
    }

    public boolean hasUser(String userId) {
        return getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst().isPresent();
    }

    public void addManager(Manager manager) {
        managers.add(manager);

    }

    public void removeChannel(Channel channel) {
        channel.setWorkspace(null);
        channels.remove(channel);
    }


}
