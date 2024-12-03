package kanban.back.service.projectManagement;

import jakarta.validation.Valid;
import kanban.back.controller.projectManagement.request.ProjectCreateRequest;
import kanban.back.domain.model.Project;
import kanban.back.domain.model.ProjectMember;
import kanban.back.port.out.ProjectPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kanban.back.mapper.ProjectMapper.toDomain;

@Service
@AllArgsConstructor
public class ProjectManagementService {

    private ProjectPort projectPort;

    public Project createProject(@Valid ProjectCreateRequest request) {
        return projectPort.create(toDomain(request));
    }

    public Map<Project, List<ProjectMember>> getProjectByUsername(String username) {
        var projects =  projectPort.getProjectByUsername(username);
        var map = new HashMap<Project, List<ProjectMember>>();
        projects.stream().forEach(project -> {
            var projectMembers = projectPort.getMembers(project);
            map.put(project, projectMembers);
        });
        return map;
    }

    public Map<Project, List<ProjectMember>> getProjectById(Integer id) {
        var map = new HashMap<Project, List<ProjectMember>>();
        var project =  projectPort.getById(id);
        var projectMember = projectPort.getMembers(project);
        map.put(project, projectMember);
        return map;
    }
}
