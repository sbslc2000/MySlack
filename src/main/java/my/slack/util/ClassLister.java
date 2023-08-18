package my.slack.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ClassLister {
    public List<Class<?>> listClassFiles(String directory, String packageName) {
        List<String> classNames = new ArrayList<>();
        File folder = new File(directory);

        if (!folder.exists()) {
            log.error("Listing 하고자 하는 패키지가 존재하지 않습니다. 패키지명: {}", packageName);
            return null;
        }
        String packagePath = packageName.replace('.', '/');
        File packageFolder = new File(folder, packagePath);
        log.debug(String.valueOf(packageFolder.listFiles()));

        for (File file : packageFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                // Remove ".class" and add to the list
                classNames.add(packageName+"."+file.getName().replace(".class", ""));
            }
        }
        try {
            return classNameToClass(classNames);
        } catch (ClassNotFoundException e) {
            log.error("클래스를 찾을 수 없습니다. 패키지명: {}", packageName);
            e.printStackTrace();
            return null;
        }
    }

    private List<Class<?>> classNameToClass(List<String> classNames) throws ClassNotFoundException {
        List<Class<?>> res = new ArrayList<>();
        for (String s : classNames) {
            Class<?> aClass = Class.forName(s);
            res.add(aClass);
        }
        return res;
    }
}
