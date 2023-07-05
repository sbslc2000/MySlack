package my.slack.domain.channel.model;

import lombok.Getter;
import my.slack.domain.message.Message;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChannelDto {
    private Long id;
    private String name;
    private String description;
    private User creator;
    private List<User> members;
    private LocalDateTime createdAt;

    public static ChannelDto of(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.id = channel.getId();
        channelDto.name = channel.getName();
        channelDto.description = channel.getDescription();
        channelDto.creator = channel.getCreator();
        channelDto.members = channel.getMembers();
        channelDto.createdAt = channel.getCreatedAt();
        return channelDto;
    }
}
