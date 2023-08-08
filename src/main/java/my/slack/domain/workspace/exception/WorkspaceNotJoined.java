package my.slack.domain.workspace.exception;

import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;

public class WorkspaceNotJoined extends ClientFaultException {
    public WorkspaceNotJoined() {
        super(ErrorCode.WORKSPACE_NOT_JOINED);
    }
}
