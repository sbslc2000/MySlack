package my.slack.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"isSuccess","message","result"})
public class BaseResponse<T> extends BaseResponseTemplate{

    private static final String DEFAULT_MESSAGE = "요청에 성공하였습니다.";

    public BaseResponse() {
        super(true,DEFAULT_MESSAGE);
    }
    public BaseResponse(T result) {
        super(result,true, DEFAULT_MESSAGE);
    }

    public BaseResponse(T result, String message) {
        super(result, true, message);
    }
}