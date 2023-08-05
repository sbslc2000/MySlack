package my.slack.domain.message;

import my.slack.domain.message.model.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMessageRepository {

    private Long sequence = 1L;
    private final Map<Long, Message> repository = new ConcurrentHashMap<>();

    public Long save(Message message) {
        message.setId(sequence++);
        repository.put(message.getId(), message);
        return message.getId();
    }

    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    public void delete(Message message) {
        repository.remove(message.getId());
    }
}
