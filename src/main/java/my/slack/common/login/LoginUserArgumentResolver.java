package my.slack.common.login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.ErrorCode;
import my.slack.api.exception.ServerFaultException;
import my.slack.common.login.model.LoginInfo;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = User.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        LoginInfo loginInfo = (LoginInfo) httpSession.getAttribute("loginInfo");
        return userRepository.findById(loginInfo.getUserId()).orElseThrow(() -> new ServerFaultException(ErrorCode.INTERNAL_SERVER_ERROR,"로그인 정보와 DB 정보가 불일치 합니다."));
    }
}
