package my.slack.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.slack.api.exception.ClientFaultException;
import my.slack.domain.user.exception.UserNotFound;
import my.slack.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static my.slack.api.ErrorCode.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public boolean isExistUserId(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public User findById(String id) {
        User user = findUser(id);
        return user;
    }

    public User createUser(String id, String email, String nickname, String profileImage) {
        User user = new User(id, email, nickname, profileImage);
        return userRepository.save(user);
    }

    //private method
    private User findUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);
    }

}
