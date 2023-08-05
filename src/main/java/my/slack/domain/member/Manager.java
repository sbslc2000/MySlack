package my.slack.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.common.model.BaseTimeEntity;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;

@Entity
@Getter
@NoArgsConstructor
public class Manager extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;
}
