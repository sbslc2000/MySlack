package my.slack.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class WebSocketRequestDto {
    private String message;
    private JsonNode body;
    
}
