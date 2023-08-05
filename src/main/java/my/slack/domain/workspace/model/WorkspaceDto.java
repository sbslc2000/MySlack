package my.slack.domain.workspace.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.slack.domain.user.model.User;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDto {
    private String id;
    private String name;
    private User creator;


    public static WorkspaceDto of(Workspace workspace) {
        return new WorkspaceDto(workspace.getId(), workspace.getName(), workspace.getCreator());
    }
}
