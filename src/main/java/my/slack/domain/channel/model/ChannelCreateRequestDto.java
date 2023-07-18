package my.slack.domain.channel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import my.slack.domain.user.model.User;

import java.util.List;


@Getter
@NoArgsConstructor
public class ChannelCreateRequestDto {
    private String name;
    private String description;
    private boolean isPrivate;

    public ChannelCreateRequestDto(String name, String description, boolean isPrivate) {
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
    }
}
