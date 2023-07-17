package my.slack.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

/**
 * 웹소켓 요청 값을 담는 객체
 * Payload로부터 파싱되어 message와 body로 나뉘어 저장됨
 * 모든 웹소켓 메시지는 아래 형식을 통해 받아야함.
 * body는 JSON 형식임
 */
@Getter
public class WebSocketRequestDto {
    private String message;
    private JsonNode body;
    
}
