package my.slack.domain.workspace;

import com.fasterxml.jackson.core.JsonProcessingException;
import my.slack.SlackApplicationTests;
import my.slack.api.response.BaseResponse;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class WorkspaceIntegratedTest extends SlackApplicationTests {



    @Test
    @DisplayName("워크스페이스 단일 조회 테스트 : 성공")
    void getWorkspace() throws JsonProcessingException {
        //given
        String workspaceId = "workspace1";
        String url = "/api/workspaces/" + workspaceId;

        //when

        ResponseEntity<BaseResponse> response = restTemplate.getForEntity(url, BaseResponse.class);
        /*
        ParameterizedTypeReference<BaseResponse<WorkspaceDto>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<BaseResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                typeRef // 제네릭 타입 지정
        );

         */

        BaseResponse body = response.getBody();
        WorkspaceDto result = extractResult(body, WorkspaceDto.class);




        /*

        BaseResponse responseBody = response.getBody();
        Object result = responseBody.getResult();
        System.out.println(responseBody);
        System.out.println(responseBody.getClass().getTypeName());
        System.out.println(responseBody.getResult());
        System.out.println(result.getClass().getTypeName());

        TypeReference<BaseResponse<WorkspaceDto>> typeRef = new TypeReference<>() {};
        String jsonString = objectMapper.writeValueAsString(responseBody);
        BaseResponse<WorkspaceDto> res = objectMapper.readValue(jsonString, typeRef);


         */

        //String str = objectMapper.writeValueAsString(responseBody.getResult());
        //WorkspaceDto result = objectMapper.readValue(str, WorkspaceDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getId()).isEqualTo(workspaceId);
    }

    @Test
    @DisplayName("워크스페이스 단일 조회 테스트 : 성공")
    void getWorkspace2() throws JsonProcessingException {
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
        WorkspaceDto result1 = (WorkspaceDto)body.getResult();


    }

}
