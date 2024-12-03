package kanban.back.port.out.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {

    String generateToken(String email);


    String extractEmail(String token);


    boolean validateToken(String token, UserDetails userDetails);

}
