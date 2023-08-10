package my.slack.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionsFactoryImpl implements WebSocketSessionsFactory{
    @Override
    public Set<WebSocketSession> getSessions() {
        Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
        return sessions;
    }
}
