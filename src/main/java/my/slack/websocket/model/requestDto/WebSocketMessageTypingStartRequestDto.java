package my.slack.websocket.model.requestDto;

import lombok.Getter;

@Getter
public class WebSocketMessageTypingStartRequestDto {
    private String workspaceId;
    private Long channelId;
    private String userId;
}
