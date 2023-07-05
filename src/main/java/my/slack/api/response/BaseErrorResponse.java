package my.slack.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import my.slack.api.ErrorCode;
import my.slack.api.exception.BaseException;

@Getter
@JsonPropertyOrder({"isSuccess","errorCode","message","result"})
public class BaseErrorResponse<T> extends BaseResponseTemplate {

    private ErrorCode errorCode;

    public int getErrorCode() {
        return errorCode.getCode();
    }

    public BaseErrorResponse(BaseException e) {
        super(false, e.getMessage());
        this.errorCode = e.getErrorCode();
    }
    public BaseErrorResponse(ErrorCode errorCode) {
        super(false, errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public BaseErrorResponse(ErrorCode errorCode,String message) {
        super(false, message);
        this.errorCode = errorCode;
    }
    public BaseErrorResponse(ErrorCode errorCode, String message, T result) {
        super(result, false, message);
        this.errorCode = errorCode;
    }
}