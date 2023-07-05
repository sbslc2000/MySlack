package my.slack.websocket.dispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappingTable {
    private Map<String, MappingInfo> table = new HashMap<>();

    public void print() {
        System.out.println("MappingTable");
        table.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
    }

    public void add(String key, MappingInfo info) {
        table.put(key, info);
    }

    public MappingInfo get(String key) {
        return table.get(key);
    }

    public void afterInit() {
        Collections.unmodifiableMap(table);
    }
}
