package my.slack.websocket.dispatcher;

import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Getter
public class MappingInfo {
    private final Class<?> clazz;
    private final Method method;
    private final Parameter[] parameters;

    public MappingInfo(Class clazz,Method method, Parameter[] parameters) {
        this.method = method;
        this.clazz = clazz;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return ("{MappingInfo clazz: " + clazz.getName() + ", method: " + method.getName() + ", parameters: " + parameters.length + "}");
    }
}
