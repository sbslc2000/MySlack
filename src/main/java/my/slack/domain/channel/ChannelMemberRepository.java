package my.slack.domain.channel;

import my.slack.domain.channel.model.ChannelMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
}
