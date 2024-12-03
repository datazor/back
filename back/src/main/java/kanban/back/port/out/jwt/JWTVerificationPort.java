package kanban.back.port.out.jwt;

import kanban.back.domain.model.LoginObject;
import kanban.back.domain.model.User;
import kanban.back.port.out.UserPort;
import kanban.back.port.out.VerificationPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class JWTVerificationPort implements VerificationPort {

    private final IJWTService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserPort userPort;


    public JWTVerificationPort(IJWTService jwtService, AuthenticationManager authenticationManager, UserPort userPort) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userPort = userPort;
    }

    @Override
    public LoginObject verify(User user) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                return LoginObject.builder()
                        .token(jwtService.generateToken(user.getEmail()))
                        .user(
                                userPort.find(user.getEmail())
                                        .orElseThrow()
                        )
                        .build();

            }
        }
        catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Invalid email or password", e);
        }
        return null;
    }
}
