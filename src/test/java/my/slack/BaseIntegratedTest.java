package my.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.slack.api.response.BaseResponse;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
        (
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = {
                        "spring.config.location=classpath:application-test.yml"
                }
        )
@Disabled
@AutoConfigureMockMvc
@Transactional
public class BaseIntegratedTest {
    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T extractResult(BaseResponse response, Class<T> clazz) throws JsonProcessingException {
        Object result = response.getResult();
        String parsedString = objectMapper.writeValueAsString(result);
        return objectMapper.readValue(parsedString, clazz);
    }

    protected BaseResponse getBaseResponse(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(contentAsString, BaseResponse.class);
    }
}
