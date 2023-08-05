package my.slack.domain.channel;

import my.slack.domain.channel.model.Channel;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryChannelRepository {
    private Long sequence = 1L;
    private final Map<Long, Channel> repository = new ConcurrentHashMap<>();

    public Long save(Channel channel) {
        channel.setId(sequence++);
        repository.put(channel.getId(), channel);
        return channel.getId();
    }

    public Optional<Channel> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    public void delete(Channel channel) {
        repository.remove(channel.getId());
    }
}
