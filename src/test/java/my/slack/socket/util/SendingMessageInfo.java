package my.slack.socket.util;

public class SendingMessageInfo {
    private String userId;
    private String body;

    public SendingMessageInfo(String userId, String body) {
        this.userId = userId;
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }
}
