package my.slack.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateRequestDto {
    private String id;
    private String email;
    private String nickname;
    private String profileImage;
}
