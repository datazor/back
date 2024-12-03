package lm.mr.pmt.port.out.jwt;

import org.springframework.security.authentication.BadCredentialsException;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String invalid_email_or_password, BadCredentialsException e) {
    }
}
