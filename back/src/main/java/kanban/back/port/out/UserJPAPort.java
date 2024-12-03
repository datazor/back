package kanban.back.port.out;

import kanban.back.domain.model.User;
import kanban.back.mapper.UserMapper;
import kanban.back.port.out.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserJPAPort implements UserPort {

    UserRepository userRepository;

    public UserJPAPort(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(UserMapper.mapToEntity(user));
    }

    @Override
    public Optional<User> find(String email) {
        return Optional.of(UserMapper.mapToDomain(userRepository.findByEmail(email)));
    }
}
