package my.slack.domain.user;

import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u join Member m on u = m.user where m.workspace = :workspace and u.nickname like %:nickname%")
    List<User> findByWorkspaceAndNicknameContaining(Workspace workspace, String nickname);
}
