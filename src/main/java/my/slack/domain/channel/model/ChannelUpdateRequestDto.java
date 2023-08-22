package my.slack.domain.channel.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChannelUpdateRequestDto {
    private String name;
}
