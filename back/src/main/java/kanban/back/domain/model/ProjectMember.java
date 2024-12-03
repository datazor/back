package kanban.back.domain.model;

import kanban.back.port.out.repository.Membership;
import kanban.back.port.out.repository.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProjectMember {
    private Long projectId;
    private Integer userId;
    private String username;
    private Role role;
    private Membership membership;
}
