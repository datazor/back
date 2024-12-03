package kanban.back.mapper;

import kanban.back.controller.userManagement.response.LoginResponse;
import kanban.back.controller.userManagement.response.UserResponse;
import kanban.back.domain.model.LoginObject;
import kanban.back.domain.model.User;

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
