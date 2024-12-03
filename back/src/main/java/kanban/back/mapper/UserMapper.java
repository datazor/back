package kanban.back.mapper;

import kanban.back.controller.userManagement.request.UserLoginRequest;
import kanban.back.controller.userManagement.request.UserRegisterRequest;
import kanban.back.domain.model.User;
import kanban.back.port.out.repository.UserEntity;

public class UserMapper {

    public static User mapToDomain(UserRegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .build();
    }

    public static User mapToDomain(UserEntity user) {
        return User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public static User mapToDomain(UserLoginRequest userLoginRequest) {
        return User.builder()
                .email(userLoginRequest.getEmail())
                .password(userLoginRequest.getPassword())
                .build();
    }

    public static UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
