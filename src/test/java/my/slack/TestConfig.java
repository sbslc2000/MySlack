package my.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.slack.socket.util.MockActiveUserService;
import my.slack.socket.util.SendingMessageRecorder;
import my.slack.socket.util.TestWebSocketSessionsFactory;
import my.slack.websocket.WebSocketSessionsFactory;
import my.slack.websocket.service.ActiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

@Configuration
public class TestConfig {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private ApplicationContext ac;

    @Primary
    @Bean
    public WebSocketSessionsFactory webSocketSessionsFactory() {
        return new TestWebSocketSessionsFactory(sendingMessageRecorder());
    }

    @Primary
    @Bean
    public ActiveUserService activeUserService() {
        return new MockActiveUserService();
    }

    @Bean
    public SendingMessageRecorder sendingMessageRecorder() {
        return new SendingMessageRecorder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            Arrays.stream(ac.getBeanDefinitionNames()).forEach(System.out::println);
        };
    }

}
