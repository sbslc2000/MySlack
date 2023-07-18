package my.slack.domain.workspace;

import jakarta.transaction.Transactional;
import my.slack.api.response.BaseResponse;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.WorkspaceDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql("/dummyDataSet.sql")
@Transactional
class WorkspaceControllerTest {

    @Autowired
    private WorkspaceController workspaceController;

    @Test
    @DisplayName("워크스페이스 조회 테스트 : 성공")
    void getWorkspace() {
        //given
        String workspaceId = "workspace1";


        //when
        BaseResponse<WorkspaceDto> response = workspaceController.getWorkspace(workspaceId);

        //then
        WorkspaceDto result = (WorkspaceDto) response.getResult();
        assertThat(result.getId()).isEqualTo(workspaceId);
    }

    @Test
    void getInviteLink() {
    }

    @Test
    @DisplayName("워크스페이스 멤버 조회 테스트 : 성공")
    void getMembersByWorkspace() {
        //given
        String workspaceId = "workspace1";
        String userId = "user1";

        //when
        BaseResponse response = workspaceController.getMembersByWorkspace(workspaceId,userId);

        //then
        List<User> result = (List<User>) response.getResult();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void createWorkspace() {
    }

    @Test
    void deleteWorkspace() {
    }

    @Test
    void addUserToWorkspace() {
    }
}