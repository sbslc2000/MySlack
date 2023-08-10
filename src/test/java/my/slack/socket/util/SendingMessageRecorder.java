package my.slack.socket.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SendingMessageRecorder {

    private Set<SendingMessageInfo> messages = new HashSet<>();

    public void record(String userId, String message) {
        messages.add(new SendingMessageInfo(userId, message));
    }

    public int size() {
        return messages.size();
    }

    public void clear() {
        messages.clear();
    }

    public Set<SendingMessageInfo> getMessages() {
        return messages;
    }
}
