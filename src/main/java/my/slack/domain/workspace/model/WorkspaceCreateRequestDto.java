package my.slack.domain.workspace.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WorkspaceCreateRequestDto {
    private String name;
    private List<String> invitees;
    private String channel;
}
