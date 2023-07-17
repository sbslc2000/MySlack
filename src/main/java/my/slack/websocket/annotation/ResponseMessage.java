package my.slack.websocket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 어노테이션 정보를 런타임에 유지
@Target(ElementType.METHOD)
public @interface ResponseMessage {
    String value();
}
