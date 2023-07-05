package my.slack.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    @GetMapping(value={"","/workspace/create"})
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/workspace/enter/{code}")
    public String enter(@PathVariable String code) {
        return "forward:/index.html?code="+code;
    }

    @GetMapping("/app/workspace/{workspaceId}")
    public String workspace(@PathVariable String workspaceId) {
        return "forward:/index.html?workspaceId="+workspaceId;
    }
}
