package lm.mr.pmt.service.userManagement;

import lm.mr.pmt.domain.model.LoginObject;
import lm.mr.pmt.domain.model.User;

import java.util.Optional;

public interface UserManagement {
    void register(User user);
    Optional<LoginObject> verify(User user);
}
