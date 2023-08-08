package my.slack.domain.workspace.exception;

import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;

public class WorkspaceNotFound extends ClientFaultException {
    public WorkspaceNotFound() {
        super(ErrorCode.WORKSPACE_NOT_FOUND);
    }
}
