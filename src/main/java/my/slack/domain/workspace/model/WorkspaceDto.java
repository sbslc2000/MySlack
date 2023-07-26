package my.slack.domain.workspace.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;


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
