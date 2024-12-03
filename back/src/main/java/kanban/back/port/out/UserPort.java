package kanban.back.port.out;

import kanban.back.domain.model.User;

import java.util.Optional;

public interface UserPort {
    void save(User user);
    Optional<User> find(String email);
}
