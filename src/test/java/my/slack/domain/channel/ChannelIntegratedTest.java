package my.slack.domain.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.slack.BaseIntegratedTest;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.model.LoginInfo;
import my.slack.domain.channel.ChannelService;
import my.slack.domain.channel.model.*;
import my.slack.domain.message.model.MessageDto;
import my.slack.domain.workspace.model.Workspace;
import my.slack.socket.util.SendingMessageInfo;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChannelIntegratedTest extends BaseIntegratedTest {

    private static final String BASE_URL = "/api/channels";

    @Test
    @DisplayName("채널 생성 : 성공")
    void createPublicChannel() throws Exception {

        String channelName = "newChannelName";
        String description = "description for newChannelName";
        Boolean isPrivate = false;
        String workspaceId = "workspace1";
        ChannelCreateRequestDto channelCreateRequestDto = new ChannelCreateRequestDto(workspaceId,channelName,description,isPrivate);
        String requestBody = objectMapper.writeValueAsString(channelCreateRequestDto);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo",getLoginInfo("user1"));


        Workspace workspace = workspaceRepository.findById(workspaceId).get();
        int beforeWorkspaceChannelSize = workspace.getChannels().size();
        long beforeChannelRepositorySize = channelRepository.count();


        //when
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        BaseResponse baseResponse = getBaseResponse(mvcResult);
        ChannelDto result = extractResult(baseResponse, ChannelDto.class);

        //then
        Workspace afterWorkspace = workspaceRepository.findById(workspaceId).get();
        int afterWorkspaceChannelSize = afterWorkspace.getChannels().size();
        long afterChannelRepositorySize = channelRepository.count();

        assertThat(result.getName()).isEqualTo(channelName);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.isPrivate()).isEqualTo(isPrivate);
        assertThat(afterWorkspaceChannelSize).isEqualTo(beforeWorkspaceChannelSize + 1);
        assertThat(afterChannelRepositorySize).isEqualTo(beforeChannelRepositorySize + 1);

        assertRefreshChannelListSocketMessage();
    }

    @Test
    @DisplayName("채널 이름 변경: 성공")
    void updateChannel() throws Exception {
        Long channelId = 3L;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo",getLoginInfo("user1"));

        ChannelUpdateRequestDto dto = new ChannelUpdateRequestDto("newChannelName");

        String requestBody = objectMapper.writeValueAsString(dto);

        //when
        MvcResult mvcResult = mockMvc.perform(patch(BASE_URL + "/" + channelId).content(requestBody)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BaseResponse baseResponse = getBaseResponse(mvcResult);
        ChannelDto channelDto = extractResult(baseResponse, ChannelDto.class);

        //then
        Channel channel = channelRepository.findById(3L).get();
        assertThat(channel.getName()).isEqualTo("newChannelName");
        assertThat(channelDto.getName()).isEqualTo("newChannelName");


    }



    @Test
    @DisplayName("채널 멤버 추가: 성공")
    void addChannelMember() throws Exception {
        //given
        String workspaceId = "workspace1";
        Long channelId = 2L;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo",getLoginInfo("user2"));

        ChannelMemberCreateRequestDto dto = new ChannelMemberCreateRequestDto("user4");
        String requestBody = objectMapper.writeValueAsString(dto);

        Channel channel = channelRepository.findById(channelId).get();
        int beforeChannelMemberSize = channel.getMembers().size();

        long beforeChannelMemberCount = channelMemberRepository.count();

        //when
            MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/" + channelId + "/members")
                        .session(session)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        //then
        BaseResponse baseResponse = getBaseResponse(mvcResult);
        List result = extractResult(baseResponse, List.class);

        Channel afterChannel = channelRepository.findById(channelId).get();
        int afterChannelMemberSize = afterChannel.getMembers().size();

        long afterChannelMemberCount = channelMemberRepository.count();

        assertThat(result.size()).isEqualTo(4);
        assertThat(afterChannelMemberSize).isEqualTo(beforeChannelMemberSize + 1);
        assertThat(afterChannelMemberCount).isEqualTo(beforeChannelMemberCount + 1);

        assertRefreshChannelListSocketMessage();
    }

    private void assertRefreshChannelListSocketMessage() {
        //WebSocketMessage가 2개가 생성
        Set<String> targetUserIds = Set.of("user1","user2");

        assertThat(sendingMessageRecorder.size()).isEqualTo(2);
        Set<SendingMessageInfo> messages = sendingMessageRecorder.getMessages();

        //Message 가 정상적으로 전달됐는지
        for(SendingMessageInfo message : messages) {
            assertThat(message.getBody()).contains("REFRESH_CHANNEL_LIST");
        }

        //대상이 정상적으로 전달됐는지
        Set<String> targetResult = messages.stream()
                .map(m -> m.getUserId())
                .collect(Collectors.toSet());

        assertThat(targetResult).isEqualTo(targetUserIds);
    }

    @Test
    @DisplayName("채널 메시지 조회 테스트: 성공")
    void getMessages() throws Exception {
        Long channelId = 1L;
        String url = BASE_URL+ "/" + channelId + "/messages";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginInfo",getLoginInfo("user1"));

        //when
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .session(session))
                .andExpect(status().isOk())
                .andReturn();

        BaseResponse baseResponse = getBaseResponse(mvcResult);
        List<MessageDto> result = extractResult(baseResponse, List.class);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

}
