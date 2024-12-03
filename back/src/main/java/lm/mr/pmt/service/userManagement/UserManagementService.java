package lm.mr.pmt.service.userManagement;

import lm.mr.pmt.domain.model.LoginObject;
import lm.mr.pmt.domain.model.User;
import lm.mr.pmt.port.out.UserPort;
import lm.mr.pmt.port.out.VerificationPort;
import lm.mr.pmt.service.encoder.Encoder;
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
