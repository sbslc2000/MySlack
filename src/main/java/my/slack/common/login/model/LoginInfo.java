package my.slack.common.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoginInfo {
    private String userId;
    private LocalDateTime loginTime;
    private LocalDateTime lastAccessTime;
}
