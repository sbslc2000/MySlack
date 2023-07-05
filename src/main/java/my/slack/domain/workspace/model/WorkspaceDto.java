package my.slack.domain.workspace.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class WorkspaceDto {
    private String id;
    private String name;
    private User creator;



    public static WorkspaceDto of(Workspace workspace) {
        return new WorkspaceDto(workspace.getId(), workspace.getName(), workspace.getCreator());
    }
}
