package my.slack.socket.util;

import my.slack.common.login.model.LoginInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockWebSocketSession implements WebSocketSession {

    private String sessionId;
    private String userId;
    private SendingMessageRecorder recorder;

    public MockWebSocketSession(String sessionId,String userId, SendingMessageRecorder recorder) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.recorder = recorder;
    }

    @Override
    public String toString() {
        return "MockWebSocketSession{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {

        Map<String,Object> attributes = new ConcurrentHashMap<>();
        attributes.put("loginInfo", new LoginInfo(userId,null,null));
        return attributes;
    }


    @Override
    public Principal getPrincipal() {
        return null;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public String getAcceptedProtocol() {
        return null;
    }

    @Override
    public void setTextMessageSizeLimit(int messageSizeLimit) {

    }

    @Override
    public int getTextMessageSizeLimit() {
        return 0;
    }

    @Override
    public void setBinaryMessageSizeLimit(int messageSizeLimit) {

    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return 0;
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return null;
    }

    @Override
    public void sendMessage(WebSocketMessage<?> message) throws IOException {
        //System.out.println("message = " + message.getPayload());
        recorder.record(userId, (String) message.getPayload());
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void close(CloseStatus status) throws IOException {

    }
}
