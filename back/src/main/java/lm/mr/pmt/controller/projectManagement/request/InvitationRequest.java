package lm.mr.pmt.controller.projectManagement.request;

import lombok.Data;

@Data
public class InvitationRequest {
    private String email;
    private Long projectId;
    private String invitedById;
    private String role;
}
