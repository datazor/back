package lm.mr.pmt.controller.projectManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lm.mr.pmt.controller.projectManagement.request.ChangeRoleRequest;
import lm.mr.pmt.controller.projectManagement.request.InvitationRequest;
import lm.mr.pmt.controller.projectManagement.request.RespondRequest;
import lm.mr.pmt.port.out.ProjectPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectMemberController {

    private final ProjectPort projectPort;

    @Autowired
    public ProjectMemberController(ProjectPort projectPort) {
        this.projectPort = projectPort;
    }

    @PutMapping("/{projectId}/members/{userId}/role")
    @Operation(summary = "Change User Role", description = "Updates the role of a user in a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User role updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project or user not found"),
            @ApiResponse(responseCode = "400", description = "Invalid role specified"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> changeUserRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestBody ChangeRoleRequest request) {
        projectPort.changeUserRole(projectId, userId, request.getNewRole());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Invite a user to a project",
            description = "This endpoint allows an administrator to invite a user to a project by their email. The invitation includes the project ID, role, and the inviter's ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User invited successfully"),
            @ApiResponse(responseCode = "404", description = "User or project not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteUser(@RequestBody InvitationRequest invitationRequest) {
        projectPort.inviteUser(invitationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/invite/respond")
    public ResponseEntity<String> respondToInvitation(@RequestBody RespondRequest invitationRequest) {
        try {
            projectPort.respondToInvitation(
                    invitationRequest.getProjectId(),
                    invitationRequest.isAccept(),
                    invitationRequest.getUsername()
            );
            return ResponseEntity.ok("Response recorded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the invitation response");
        }
    }
}

