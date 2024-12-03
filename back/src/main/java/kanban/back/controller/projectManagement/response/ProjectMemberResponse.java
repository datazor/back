package kanban.back.controller.projectManagement.response;

import kanban.back.port.out.repository.Role;
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
