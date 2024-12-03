package lm.mr.pmt.port.out;

import lm.mr.pmt.domain.model.User;

import java.util.Optional;

public interface UserPort {
    void save(User user);
    Optional<User> find(String email);
}
