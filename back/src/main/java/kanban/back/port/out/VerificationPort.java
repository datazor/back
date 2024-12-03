package kanban.back.port.out;

import kanban.back.domain.model.LoginObject;
import kanban.back.domain.model.User;

public interface VerificationPort {
    LoginObject verify(User user);
}
