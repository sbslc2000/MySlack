package my.slack.common.login;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public void socialLogin(String code, String registrationId, HttpSession session) {

        /*
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);
         */

        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        String id = userResourceNode.get("id").asText();
        log.info("{}", userResourceNode);

        //최초 로그인 시
        if(!userService.isExistUserId(id)) {
            String email = userResourceNode.get("email").asText();
            String nickname = userResourceNode.get("name").asText();
            String profileImage = userResourceNode.get("picture").asText();

            userService.createUser(id, email, nickname,profileImage);
        }


        //로그인
        session.setAttribute("userId", id);
        log.info("로그인 성공 : userId = {}", id);
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2."+registrationId+".client-id");
        String clientSecret = env.getProperty("oauth2."+registrationId+".client-secret");
        String redirectUri = env.getProperty("oauth2."+registrationId+".redirect-uri");
        String tokenUri = env.getProperty("oauth2."+registrationId+".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity httpEntity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, httpEntity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2." + registrationId + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class)
                .getBody();
    }

    public void logout(HttpSession session) {
        session.removeAttribute("userId");
    }
}