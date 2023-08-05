package my.slack.domain.user;

import my.slack.domain.user.model.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryUserRepository {
    private final Map<String, User> repository = new ConcurrentHashMap<>();

    public Optional<User> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    public String createUser(User user) {
        repository.put(user.getId(), user);
        return user.getId();
    }

    public void deleteUser(User user) {
        repository.remove(user.getId());
    }
}
