package my.slack.domain.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.slack.BaseIntegratedTest;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.model.LoginInfo;
import my.slack.domain.channel.model.ChannelCreateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ChannelIntegratedTest extends BaseIntegratedTest {

    @Test
    @DisplayName("채널 생성")
    void createChannel() throws Exception {
        // given
        ChannelCreateRequestDto requestDto = new ChannelCreateRequestDto("testChannel1", "testChannel", false);
        String requestBody = objectMapper.writeValueAsString(requestDto);


        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo", new LoginInfo("user1", LocalDateTime.now(), LocalDateTime.now()));


        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/channels")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn();
        // then

        BaseResponse baseResponse = getBaseResponse(mvcResult);
        extractResult(baseResponse,ChannelD);
    }
}
