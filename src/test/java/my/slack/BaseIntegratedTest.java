package my.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.model.LoginInfo;
import my.slack.domain.channel.ChannelMemberRepository;
import my.slack.domain.channel.ChannelRepository;
import my.slack.domain.message.MessageRepository;
import my.slack.domain.user.UserRepository;
import my.slack.domain.workspace.WorkspaceRepository;
import my.slack.socket.util.SendingMessageRecorder;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

@SpringBootTest
        (
                webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
                properties = {
                        "spring.config.location=classpath:application-test.yml"
                }
        )
@Disabled
@AutoConfigureMockMvc
@Transactional
@Import(TestConfig.class)
public class BaseIntegratedTest {

    @BeforeAll
    public static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {

            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/dummyDataSet.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void tearDown(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/truncateAll.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void after() {
        sendingMessageRecorder.clear();
    }


    @Autowired protected TestRestTemplate restTemplate;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected WorkspaceRepository workspaceRepository;
    @Autowired protected ChannelRepository channelRepository;
    @Autowired protected ChannelMemberRepository channelMemberRepository;
    @Autowired protected MessageRepository messageRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected SendingMessageRecorder sendingMessageRecorder;

    protected <T> T extractResult(BaseResponse response, Class<T> clazz) throws JsonProcessingException {
        Object result = response.getResult();
        String parsedString = objectMapper.writeValueAsString(result);
        return objectMapper.readValue(parsedString, clazz);
    }

    protected BaseResponse getBaseResponse(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(contentAsString, BaseResponse.class);
    }

    protected LoginInfo getLoginInfo(String userId) {
        return new LoginInfo(userId, LocalDateTime.now(), LocalDateTime.now());
    }


}
