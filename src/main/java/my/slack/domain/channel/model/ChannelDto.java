package my.slack.domain.channel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import my.slack.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChannelDto {
    private Long id;
    private String name;
    private String description;
    private User creator;
    private List<User> members;

    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private LocalDateTime createdAt;

    public static ChannelDto of(Channel channel) {
        ChannelDto channelDto = new ChannelDto();
        channelDto.id = channel.getId();
        channelDto.name = channel.getName();
        channelDto.description = channel.getDescription();
        channelDto.creator = channel.getCreator();
        channelDto.isPrivate = channel.isPrivate();
        channelDto.createdAt = channel.getCreatedAt();
        return channelDto;
    }
}
