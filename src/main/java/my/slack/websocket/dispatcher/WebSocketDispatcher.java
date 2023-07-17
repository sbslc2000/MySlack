package my.slack.websocket.dispatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.domain.user.model.User;
import my.slack.websocket.annotation.MessageMapping;
import my.slack.websocket.annotation.ResponseMessage;
import my.slack.websocket.annotation.WebSocketSessionAttribute;
import my.slack.websocket.model.WebSocketMessageRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketDispatcher {
    private static final String SERVICE_PACKAGE_NAME = "my.slack.websocket.controller";

    private final ObjectMapper objectMapper;
    private MappingTable mappingTable;
    private final ApplicationContext ac;

    @PostConstruct
    public void init() {
        log.info("WebSocketDispatcher 초기화 시작");
        String packageName = SERVICE_PACKAGE_NAME;
        mappingTable = new MappingTable();

        try {
            List<Class<?>> classes = getClassesInPackage(packageName);
            log.info("Target Classes: {}개", classes.size());
            for (Class<?> clazz : classes) {
                Method[] methods = clazz.getDeclaredMethods();

                log.info("Target Class: {}, Methods: {}개", clazz.getName(), methods.length);

                for (Method method : methods) {
                    log.info("Method: {}", method.getName());
                    if(method.isSynthetic() || !Modifier.isPublic(method.getModifiers())) {
                        log.info("이건 패스");
                        continue;
                    }
                    MessageMapping requestMessage = method.getAnnotation(MessageMapping.class);
                    ResponseMessage responseMessage = method.getAnnotation(ResponseMessage.class);
                    Parameter[] parameters = method.getParameters();

                    mappingTable.add(requestMessage.value(), new MappingInfo(clazz, method, parameters, responseMessage.value()));
                }
            }

            mappingTable.afterInit();
            mappingTable.print();
            log.info("WebSocketDispatcher MappingTable 초기화 완료");
        } catch (Exception e) {
            log.error("WebSocketDispatcher 초기화 중 에러 발생");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        String path = packageName.replace('.', '/');
        log.info("path : {}", path);

        Enumeration<URL> resources = classLoader.getResources(path);


        List<String> resourceNames = new ArrayList<>();
        while(resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            log.info("resource : {}", resource);
            File packageDirectory = new File(resource.getFile());
            if (packageDirectory.exists() && packageDirectory.isDirectory()) {
                File[] files = packageDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packageName + "." + file.getName().substring(0, file.getName().lastIndexOf('.'));
                            resourceNames.add(className);
                        }
                    }
                }
            }

        }

        log.info("classNames : {}", resourceNames);

        List<Class<?>> classes = new ArrayList<>();
        for (String resourceName : resourceNames) {
            Class<?> clazz = Class.forName(resourceName);
            log.info("class.getName() = {}",clazz.getName());
            classes.add(clazz);
        }

        log.info("classes : {}", classes);
        return classes;
    }


    public WebSocketMessageRequest dispatch(String userId, String message, JsonNode body) throws Exception {
        log.info("WebSocketDispatcher.dispatch() 호출");
        log.info("message = {}", message);
        MappingInfo mappingInfo = mappingTable.get(message);
        String responseMessage = mappingInfo.getResponseMessage();
        Parameter[] parameters = mappingInfo.getParameters();

        // 빈 이름을 통해 인스턴스 가져오기
        String className = mappingInfo.getClazz()
                .getName();
        String beanName = className.substring(className.lastIndexOf('.') + 1);  // 패키지 이름 제외
        beanName = beanName.substring(0, 1)
                .toLowerCase() + beanName.substring(1);  // 첫 문자를 소문자로 변경
        log.info("beanName = {}" , beanName);

        //파라미터 파싱 and binding
        Object[] parsedParameters = new Object[parameters.length];
        List<User> targetUsers = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            log.info("parameter {} binding",i);

            WebSocketSessionAttribute paramAnnotation = parameter.getAnnotation(WebSocketSessionAttribute.class);
            //만약 파라미터에 @WebSocketSessionAttribute 어노테이션이 붙어있다면
            if(paramAnnotation != null) {
                String value = paramAnnotation.value();
                log.info("value = {}", value);
                if(value.equals("userId")) {
                    parsedParameters[i] = userId;
                    continue;
                } else if(value.equals("targetUsers")) {
                        parsedParameters[i] = targetUsers;
                        continue;
                } else {
                    log.error("value 이름이 잘못 설정되었습니다.");
                }
            } else { //파라미터에 @WebSocketSessionAttribute 어노테이션이 붙어있지 않다면
                String paramName = parameter.getName();
                Class<?> paramType = parameter.getType();

                //웹소켓 body에서 파라미터 이름으로 JsonNode를 가져오고
                //이것을 파라미터의 타입으로 변환한 Object를 해당 파라미터로 전송
                JsonNode jsonNode = body.get(paramName);
                Object parsedParam = objectMapper.treeToValue(jsonNode, paramType);
                parsedParameters[i] = parsedParam;
            }

        }


        for(Object o : parsedParameters) {
            log.info("parsedParameters = {}", o);
        }

        //요청 전송
        //응답은 컨트롤러의 반환값으로, 이것을 웹소켓 응답의 body에 담아서 전송
        Object responseBody = (Object) mappingInfo.getMethod()
                .invoke(ac.getBean(beanName), parsedParameters);




        //결과 반환
        return new WebSocketMessageRequest(responseMessage,responseBody,targetUsers);
    }

}


