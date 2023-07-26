package my.slack.domain.workspace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.slack.BaseIntegratedTest;
import my.slack.SlackApplicationTests;
import my.slack.api.response.BaseResponse;
import my.slack.common.login.model.LoginInfo;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkspaceIntegratedTest extends BaseIntegratedTest {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @BeforeAll
    public static void setup(@Autowired DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {

            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/dummyDataSet.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("워크스페이스 단일 조회 테스트 : 성공")
    void getWorkspace() throws JsonProcessingException {
        //given
        String workspaceId = "workspace1";
        String url = "/api/workspaces/" + workspaceId;

        //when
        ParameterizedTypeReference<BaseResponse<WorkspaceDto>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<BaseResponse<WorkspaceDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                typeRef // 제네릭 타입 지정
        );

        BaseResponse<WorkspaceDto> body = response.getBody();
        //then

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getResult().getId()).isEqualTo(workspaceId);
    }

    @Test
    @DisplayName("워크스페이스 단일 조회 테스트: 찾을 수 없음")
    void getWorkspaceNotFound() {
        //given
        String workspaceId = "workspace100";
        String url = "/api/workspaces/" + workspaceId;

        //when
        ParameterizedTypeReference<BaseResponse> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<BaseResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                typeRef // 제네릭 타입 지정
        );

        BaseResponse body = response.getBody();
        //then

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.isIsSuccess()).isFalse();
        assertThat(body.getMessage()).isEqualTo("존재하지 않는 워크스페이스입니다.");
    }

    @Test
    @DisplayName("워크스페이스 추가: 성공")
    void createWorkspace() throws Exception {
        //given
        String url = "/api/workspaces";
        WorkspaceCreateRequestDto requestDto = new WorkspaceCreateRequestDto("workspace100",null,"newChannel");

        String requestBody = objectMapper.writeValueAsString(requestDto);
        MockHttpSession session = new MockHttpSession();
        LoginInfo loginInfo = new LoginInfo("user1", LocalDateTime.now(), LocalDateTime.now());
        session.setAttribute("loginInfo", loginInfo);

        long before = workspaceRepository.count();
        //when
        MvcResult mvcResult = mockMvc.perform(post(url).content(requestBody).session(session).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        //then
        long after = workspaceRepository.count();

        String contentAsString = mvcResult.getResponse()
                .getContentAsString();

        BaseResponse baseResponse = objectMapper.readValue(contentAsString, BaseResponse.class);
        WorkspaceDto result = extractResult(baseResponse, WorkspaceDto.class);

        Workspace workspace = workspaceRepository.findById(result.getId())
                .get();

        //워크스페이스 정상적 생성
        assertThat(result.getName()).isEqualTo(requestDto.getName());
        assertThat(before+1).isEqualTo(after);
        assertThat(baseResponse.isIsSuccess()).isTrue();

        //채널 정상적 생성
        assertThat(workspace.getChannels().size()).isEqualTo(1);
        assertThat(workspace.getChannels().get(0).getName()).isEqualTo(requestDto.getChannel());
    }

    @Test
    @DisplayName("워크스페이스 추가: 실패 - 로그인 안함")
    void createWorkspaceFail() throws Exception {
        //given
        String url = "/api/workspaces";
        WorkspaceCreateRequestDto requestDto = new WorkspaceCreateRequestDto("workspace1", null, "newChannel");

        String requestBody = objectMapper.writeValueAsString(requestDto);
        MockHttpSession session = new MockHttpSession();

        long before = workspaceRepository.count();
        //when
        MvcResult mvcResult = mockMvc.perform(post(url).content(requestBody)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();


        //then
        long after = workspaceRepository.count();

        String contentAsString = mvcResult.getResponse()
                .getContentAsString();
        BaseResponse baseResponse = objectMapper.readValue(contentAsString, BaseResponse.class);
        WorkspaceDto result = extractResult(baseResponse, WorkspaceDto.class);

        assertThat(before).isEqualTo(after);
        assertThat(baseResponse.isIsSuccess()).isFalse();
        assertThat(baseResponse.getMessage()).isEqualTo("로그인이 필요합니다.");
    }

}
