package lm.mr.pmt.domain.model;

import lm.mr.pmt.port.out.repository.Membership;
import lm.mr.pmt.port.out.repository.Role;
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
