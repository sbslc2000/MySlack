package my.slack.domain.user.model;

import lombok.Getter;

@Getter
public class User {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;
    private boolean isActive = false;

    public void setActive(boolean active) {
        isActive = active;
    }

    public User(String id, String email, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
