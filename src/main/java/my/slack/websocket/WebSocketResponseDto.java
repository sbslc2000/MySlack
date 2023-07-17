package my.slack.websocket;

import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * WebSocketResponseDto
 *
 * 웹소켓 응답 메시지를 담는 객체
 * @param <T>
 */
@Getter
@NoArgsConstructor
public class WebSocketResponseDto<T> {
    private String message;
    private T body;

    public WebSocketResponseDto(String message,T body) {
        this.message = message;
        this.body = body;
    }
}
