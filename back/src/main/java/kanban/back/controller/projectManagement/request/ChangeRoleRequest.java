package kanban.back.controller.projectManagement.request;


import kanban.back.port.out.repository.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {
    private Role newRole;
}
