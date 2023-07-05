package my.slack.domain.workspace.model;

import lombok.Getter;

import java.util.List;

@Getter
public class WorkspaceCreateRequestDto {
    private String name;
    private List<String> invitees;
    private String channel;
}
