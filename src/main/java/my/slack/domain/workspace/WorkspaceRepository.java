package my.slack.domain.workspace;

import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace,String> {

    @Query("SELECT w FROM Workspace w " +
            "WHERE w.creator = :user " +
            "OR :user = ANY (SELECT m.user FROM w.managers m) " +
            "OR :user = ANY (SELECT mem.user FROM w.members mem)")
    List<Workspace> findByUser(User user);
}
