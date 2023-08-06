package my.slack.common.login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import my.slack.api.response.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(produces = "application/json")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public String googleLogin(@RequestParam String code, @PathVariable String registrationId, HttpSession session) {
        loginService.socialLogin(code,registrationId,session);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginInfo");
        return "redirect:/";
    }

}
