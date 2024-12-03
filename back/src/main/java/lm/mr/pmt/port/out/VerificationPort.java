package lm.mr.pmt.port.out;

import lm.mr.pmt.domain.model.LoginObject;
import lm.mr.pmt.domain.model.User;

public interface VerificationPort {
    LoginObject verify(User user);
}
