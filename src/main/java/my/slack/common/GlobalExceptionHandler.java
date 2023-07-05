package my.slack.common;

import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;
import my.slack.api.exception.ServerFaultException;
import my.slack.api.response.BaseErrorResponse;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(basePackages = {"my.slack.domain","my.slack.common"})
public class GlobalExceptionHandler {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private BaseErrorResponse<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new BaseErrorResponse<>(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ClientFaultException.class)
    private BaseErrorResponse<String> clientFaultExceptionHandler(ClientFaultException e) {
        e.printStackTrace();
        return new BaseErrorResponse<>(e);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerFaultException.class)
    private BaseErrorResponse<String> serverFaultExceptionHandler(ServerFaultException e) {
        e.printStackTrace();
        return new BaseErrorResponse<>(e);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    private BaseErrorResponse<String> servletRequestBindingExceptionHandler(ServletRequestBindingException e) {
        e.printStackTrace();
        return new BaseErrorResponse<>(ErrorCode.INVALID_REQUEST, e.getMessage());
    }



}
