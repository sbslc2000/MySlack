package my.slack.domain.workspace;

import jakarta.transaction.Transactional;
import my.slack.api.response.BaseResponse;
import my.slack.domain.user.UserRepository;
import my.slack.domain.user.model.User;
import my.slack.domain.workspace.model.Workspace;
import my.slack.domain.workspace.model.WorkspaceCreateRequestDto;
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

@Transactional
class WorkspaceControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkspaceController workspaceController;

    @Autowired
    private WorkspaceRepository workspaceRepository;

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
        User user = userRepository.findById(userId)
                .get();

        //when
        BaseResponse response = workspaceController.getMembersByWorkspace(workspaceId,user);

        //then
        List<User> result = (List<User>) response.getResult();
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void createWorkspace() {

        //given
        long before = workspaceRepository.count();
        WorkspaceCreateRequestDto dto = new WorkspaceCreateRequestDto("newWorkspace", null, "기록");

        //when
        User user1 = userRepository.findById("user1")
                .get();
        BaseResponse<WorkspaceDto> response = workspaceController.createWorkspace(user1, dto);

        //then

        long after = workspaceRepository.count();
        WorkspaceDto workspaceDto = (WorkspaceDto)response.getResult();

        Workspace workspace = workspaceRepository.findById(workspaceDto.getId())
                .get();

        assertThat(workspaceDto.getCreator().getId()).isEqualTo("user1");
        assertThat(workspaceDto.getName()).isEqualTo("newWorkspace");
        assertThat(after).isEqualTo(before+1);
        assertThat(workspace.getChannels().size()).isEqualTo(1);
    }

    @Test
    void deleteWorkspace() {
    }

    @Test
    void addUserToWorkspace() {
    }
}