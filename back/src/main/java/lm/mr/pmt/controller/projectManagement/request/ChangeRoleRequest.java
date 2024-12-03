package lm.mr.pmt.controller.projectManagement.request;


import lm.mr.pmt.port.out.repository.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {
    private Role newRole;
}
