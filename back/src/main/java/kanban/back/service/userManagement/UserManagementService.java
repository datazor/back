package kanban.back.service.userManagement;

import kanban.back.domain.model.LoginObject;
import kanban.back.port.out.UserPort;
import kanban.back.port.out.VerificationPort;
import kanban.back.service.encoder.Encoder;
import kanban.back.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserManagementService implements UserManagement{
    private final Encoder encoder;

    private final UserPort userPort;

    private final VerificationPort verificationPort;

    @Override
    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userPort.save(user);
    }

    @Override
    public Optional<LoginObject> verify(User user) {
        return Optional.of(verificationPort.verify(user));
    }
}
