package kanban.back.service.userManagement.userDetails;

import kanban.back.port.out.UserPort;
import kanban.back.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PMTUserDetailsService implements UserDetailsService {

    private UserPort userPort;

    public PMTUserDetailsService(UserPort userPort) {
        this.userPort = userPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userPort.find(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));;
        return new UserPrincipal(user);
    }
}
