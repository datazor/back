package lm.mr.pmt.mapper;

import lm.mr.pmt.controller.userManagement.response.LoginResponse;
import lm.mr.pmt.controller.userManagement.response.UserResponse;
import lm.mr.pmt.domain.model.LoginObject;
import lm.mr.pmt.domain.model.User;

public class LoginMapper {

    public static LoginResponse toLoginResponse(LoginObject loginObject) {
        return LoginResponse.builder()
                .token(loginObject.getToken())
                .user(toUserResponse(loginObject.getUser()))
                .build();
    }

    private static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
