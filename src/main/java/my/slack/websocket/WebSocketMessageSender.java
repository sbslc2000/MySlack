package my.slack.websocket;

import my.slack.websocket.model.WebSocketMessageRequest;


public interface WebSocketMessageSender{

    public void sendMessage(WebSocketMessageRequest request);
}
