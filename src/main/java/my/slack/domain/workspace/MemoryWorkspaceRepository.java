package my.slack.domain.workspace;

import my.slack.domain.workspace.model.Workspace;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemoryWorkspaceRepository {
    private Map<String, Workspace> repository = new ConcurrentHashMap<>();

    public Optional<Workspace> findById(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    public String save(Workspace workspace) {
        workspace.setId(UUID.randomUUID().toString());
        repository.put(workspace.getId(), workspace);
        return workspace.getId();
    }

    public void deleteById(String id) {
        repository.remove(id);
    }

    public List<Workspace> findByUserId(String userId) {
        return repository.values().stream().filter(workspace -> workspace.hasUser(userId)).collect(Collectors.toList());
    }


}
