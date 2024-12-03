package kanban.back.service.userManagement;

import kanban.back.domain.model.LoginObject;
import kanban.back.domain.model.User;

import java.util.Optional;

public interface UserManagement {
    void register(User user);
    Optional<LoginObject> verify(User user);
}
