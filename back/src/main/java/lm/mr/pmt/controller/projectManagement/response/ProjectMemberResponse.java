package lm.mr.pmt.controller.projectManagement.response;

import lm.mr.pmt.port.out.repository.Role;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberResponse {

    private Long id;
    private String name;
    private Role role;
}
