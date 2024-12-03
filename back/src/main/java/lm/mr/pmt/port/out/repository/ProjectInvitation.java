package lm.mr.pmt.port.out.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ProjectInvitation {
    private String username;
    private Long projectId;
    private String projectName;
    private String invitedBy;
    private String status;
    private Date createdAt;
    private boolean read;
}
