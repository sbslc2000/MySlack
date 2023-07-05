package my.slack.websocket;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
