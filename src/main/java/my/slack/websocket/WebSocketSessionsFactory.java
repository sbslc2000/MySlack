package my.slack.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface WebSocketSessionsFactory {

    Set<WebSocketSession> getSessions();
}
