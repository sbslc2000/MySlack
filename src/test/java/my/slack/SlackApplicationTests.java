package my.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.slack.api.response.BaseResponse;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
        (
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = {
                        "spring.config.location=classpath:application-test.yml"
                }
        )
@Sql("/dummyDataSet.sql")
public class SlackApplicationTests {


    @Test
    void contextLoads() {
    }
}
