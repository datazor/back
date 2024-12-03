package kanban.back.port.out;

import kanban.back.controller.projectManagement.request.InvitationRequest;
import kanban.back.domain.model.Project;
import kanban.back.domain.model.ProjectMember;
import kanban.back.port.out.repository.Role;

import java.util.List;

public interface ProjectPort {

    Project create(Project project);
    List<Project> getAll();
    List<Project> getByName(String name);

    List<ProjectMember> getMembers(Project project);

    Project getById(Integer id);

    List<Project> getProjectByUsername(String username);

    void changeUserRole(Long projectId, Long userId, Role newRole);

    void inviteUser(InvitationRequest request);

    void respondToInvitation(Long projectId, boolean accept, String username);

}
