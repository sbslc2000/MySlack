package my.slack.api.exception;

import my.slack.api.ErrorCode;

public class ServerFaultException extends BaseException {
    public ServerFaultException(ErrorCode errorCode, String message) {
        super(errorCode,message);
    }
    public ServerFaultException(ErrorCode errorCode) {
        super(errorCode);
    }
}