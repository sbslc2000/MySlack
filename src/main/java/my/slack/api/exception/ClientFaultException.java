package my.slack.api.exception;

import my.slack.api.ErrorCode;

public class ClientFaultException extends BaseException {
    public ClientFaultException(ErrorCode errorCode, String message) {
        super(errorCode,message);
    }
    public ClientFaultException(ErrorCode errorCode) {
        super(errorCode);
    }
}