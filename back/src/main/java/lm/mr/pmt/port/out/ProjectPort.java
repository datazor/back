package lm.mr.pmt.port.out;

import lm.mr.pmt.controller.projectManagement.request.InvitationRequest;
import lm.mr.pmt.domain.model.Project;
import lm.mr.pmt.domain.model.ProjectMember;
import lm.mr.pmt.port.out.repository.Role;

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
