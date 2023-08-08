package my.slack.domain.user.exception;

import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;

public class UserNotFound extends ClientFaultException {
    public UserNotFound() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
