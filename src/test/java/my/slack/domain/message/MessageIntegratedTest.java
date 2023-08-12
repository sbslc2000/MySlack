package my.slack.domain.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.slack.BaseIntegratedTest;
import my.slack.api.response.BaseResponse;
import my.slack.domain.channel.model.Channel;
import my.slack.domain.message.model.Message;
import my.slack.domain.message.model.MessageCreateRequestDto;
import my.slack.domain.message.model.MessageDto;
import my.slack.socket.util.SendingMessageInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageIntegratedTest extends BaseIntegratedTest {

    @Test
    @DisplayName("퍼블릭 채널 메시지 생성: 성공")
    void createMessageInPublicChannel() throws Exception {
        Long channelId = 1L;
        MessageCreateRequestDto messageCreateRequestDto = new MessageCreateRequestDto(channelId,"message1");
        String requestBody = objectMapper.writeValueAsString(messageCreateRequestDto);
        String url = "/api/messages?channelId="+channelId;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo",getLoginInfo("user1"));

        long beforeCount = messageRepository.count();



        //when
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .session(session)
                )
                .andExpect(status().isOk())
                .andReturn();

        BaseResponse baseResponse = getBaseResponse(mvcResult);
        MessageDto result = extractResult(baseResponse, MessageDto.class);


        //then

        long afterCount = messageRepository.count();

        Channel channel = channelRepository.findById(channelId).get();

        assertThat(afterCount).isEqualTo(beforeCount+1);
        assertThat(result.getContent()).isEqualTo(messageCreateRequestDto.getContent());
        assertThat(result.getSender().getId()).isEqualTo("user1");
        assertThat(channel.getMessages().get(channel.getMessages().size()-1))
                .isEqualTo(messageRepository.findById(result.getId()).get());

        Set<SendingMessageInfo> messages = sendingMessageRecorder.getMessages();
        assertThat(messages.size()).isEqualTo(2);
        Set<String> targetUserIds = Set.of("user1","user2");
        assertThat(messages.stream().map(SendingMessageInfo::getUserId).collect(Collectors.toSet()))
                .isEqualTo(targetUserIds);
        for(SendingMessageInfo message : messages){
            assertThat(message.getBody()).contains("REFRESH_MESSAGES");
        }
    }


}
