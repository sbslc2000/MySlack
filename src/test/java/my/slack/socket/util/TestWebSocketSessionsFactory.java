package my.slack.socket.util;

import my.slack.websocket.WebSocketSessionsFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestWebSocketSessionsFactory implements WebSocketSessionsFactory {

    private final SendingMessageRecorder recorder;

    public TestWebSocketSessionsFactory(SendingMessageRecorder sendingMessageRecorder) {
        this.recorder = sendingMessageRecorder;
    }

    @Override
    public Set<WebSocketSession> getSessions() {
        Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
        sessions.add(new MockWebSocketSession("1","user1",recorder));
        sessions.add(new MockWebSocketSession("2","user2",recorder));
        sessions.add(new MockWebSocketSession("3","user5",recorder));
        return sessions;
    }
}
