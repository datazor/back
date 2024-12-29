package kanban.back.controller.userManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kanban.back.controller.userManagement.request.UserLoginRequest;
import kanban.back.controller.userManagement.request.UserRegisterRequest;
import kanban.back.controller.userManagement.response.LoginResponse;
import kanban.back.service.userManagement.UserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static kanban.back.mapper.LoginMapper.toLoginResponse;
import static kanban.back.mapper.UserMapper.mapToDomain;

@Validated
@RestController
@RequestMapping("api/v1/user")
@Tag(name = "User Management", description = "Operations related to user management, including registration and login.")
public class UserManagementController {

    @Autowired
    private UserManagement userManagement;


    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with a username, email, and password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "409", description = "User with given email or username already exists"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/register")
    public void register(@Valid @RequestBody UserRegisterRequest request) {
        userManagement.register(mapToDomain(request));
    }


    @Operation(
            summary = "User Login",
            description = "Authenticates a user with email and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody UserLoginRequest request) {
        var loginResult = userManagement.verify(mapToDomain(request));

        return toLoginResponse(
                loginResult.orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password")
                )
        );
    }
}
