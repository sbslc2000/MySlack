package my.slack.domain.channel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ChannelCreateRequestDto {
    private String workspaceId;
    private String name;
    private String description;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    public ChannelCreateRequestDto(String workspaceId, String name, String description, boolean isPrivate) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
    }
}
