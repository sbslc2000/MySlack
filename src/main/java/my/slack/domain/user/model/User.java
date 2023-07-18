package my.slack.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    private String id;

    private String email;

    private String nickname;

    private String profileImage;

    private boolean isActive = false;

    public void setStatus(boolean active) {
        isActive = active;
    }

    public User(String id, String email, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
