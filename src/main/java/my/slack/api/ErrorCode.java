package my.slack.api;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_REQUEST(4000, "유효하지 않은 요청입니다."),
    UNAUTHORIZED(4001, "인증되지 않은 사용자입니다."),
    FORBIDDEN(4002, "권한이 없습니다."),
    VALIDATION_FAILURE(4100,"검증 오류가 발생했습니다."),
    ENTITY_NOT_FOUND(4200, "해당 엔티티를 찾을 수 없습니다."),
    JSON_PARSE_ERROR(4300, "JSON 파싱 에러가 발생하였습니다."),
    INTERNAL_SERVER_ERROR(5000, "서버 내부 에러가 발생하였습니다."),
    DATA_INTEGRITY_FAILURE(9999, "데이터 무결성 에러가 발생하였습니다."),
    BOOK_VALIDATION_ERROR(10001,"책 검증 에러가 발생하였습니다.")
    ;
    private final int code;
    private final String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}