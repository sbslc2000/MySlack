package my.slack.domain.channel.exception;

import my.slack.api.ErrorCode;
import my.slack.api.exception.ClientFaultException;

public class ChannelNotFound extends ClientFaultException {
    public ChannelNotFound() {
        super(ErrorCode.CHANNEL_NOT_FOUND);
    }
}
