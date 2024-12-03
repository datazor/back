package lm.mr.pmt.port.out;

import lm.mr.pmt.domain.model.User;
import lm.mr.pmt.mapper.UserMapper;
import lm.mr.pmt.port.out.repository.UserRepository;
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
