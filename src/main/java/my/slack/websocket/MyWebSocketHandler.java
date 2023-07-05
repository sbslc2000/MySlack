package my.slack.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.websocket.dispatcher.WebSocketDispatcher;
import my.slack.websocket.model.WebSocketMessageRequest;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyWebSocketHandler extends TextWebSocketHandler implements WebSocketMessageSender{

    private final ObjectMapper objectMapper;
    private final ActiveUserService activeUserService;
    private final WebSocketDispatcher dispatcher;

    private static Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("userId={}",session.getAttributes().get("userId"));

        sessions.add(session);
        log.info(session.getId()+" 연결됨");

        handleRequest(session,"USER_CONNECT");

        super.afterConnectionEstablished(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info(session.getId()+"로부터 메시지 수신: "+message.getPayload());
        super.handleTextMessage(session, message);

        WebSocketRequestDto request = objectMapper.readValue(message.getPayload(), WebSocketRequestDto.class);
        handleRequest(session, request.getMessage(),request.getBody());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info(session.getId()+" 연결 종료됨");

        handleRequest(session,"USER_DISCONNECT");
        super.afterConnectionClosed(session, status);
    }

    private void handleRequest(WebSocketSession session,String message, JsonNode body) {
        String userId = getUserIdFromSession(session);

        try {
            WebSocketMessageRequest res = dispatcher.dispatch(userId,message, body);
            sendMessage(res);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("WebSocket 컨트롤러에서 에러 발생");
            //sendMessage(new WebSocketMessageRequest("ERROR", null,null));
        }
    }

    private void handleRequest(WebSocketSession session, String requestType) {
        handleRequest(session,requestType,null);
    }

    @Override
    public void sendMessage(WebSocketMessageRequest req) {

        String message = req.getMessage();
        Object body = req.getBody();

        List<String> targets = req.getRefreshTarget().stream().map(u -> u.getId()).toList();
        WebSocketResponseDto dto = new WebSocketResponseDto(message,body);

        try {
            String jsonDto = objectMapper.writeValueAsString(dto);
            log.info("jsonDto: {}", jsonDto);
            sessions.stream().filter(s -> targets.contains(s.getAttributes().get("userId"))).forEach(
                    s -> {
                        try {
                            s.sendMessage(new TextMessage(jsonDto));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getUserIdFromSession(WebSocketSession session) {
        return (String)session.getAttributes().get("userId");
    }
}
