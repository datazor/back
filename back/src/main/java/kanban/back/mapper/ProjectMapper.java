package kanban.back.mapper;

import kanban.back.controller.projectManagement.request.ProjectCreateRequest;
import kanban.back.controller.projectManagement.response.ProjectMemberResponse;
import kanban.back.controller.projectManagement.response.ProjectResponse;
import kanban.back.domain.model.Project;
import kanban.back.domain.model.ProjectMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static Project toDomain(ProjectCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .build();
    }

    public static List<ProjectResponse> toResponseList(Map<Project, List<ProjectMember>> projectListMap) {
        return projectListMap.entrySet().stream().map(entry -> {
            Project project = entry.getKey();
            List<ProjectMember> projectMembers = entry.getValue();
            List<ProjectMemberResponse> memberResponses = projectMembers.stream().map(member ->
                    ProjectMemberResponse.builder()
                            .id(member.getUserId().longValue())
                            .name(member.getUsername())
                            .role(member.getRole())
                            .build()
            ).toList();

            // Build the ProjectResponse
            return ProjectResponse.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .startDate(project.getStartDate())
                    .members(memberResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    public static ProjectResponse toResponse(Map<Project, List<ProjectMember>> projectMap) {
        // Ensure the map has exactly one entry
        if (projectMap.size() != 1) {
            throw new IllegalArgumentException("Expected a map with exactly one project entry.");
        }

        // Retrieve the single entry
        Map.Entry<Project, List<ProjectMember>> entry = projectMap.entrySet().iterator().next();
        Project project = entry.getKey();
        List<ProjectMember> projectMembers = entry.getValue();

        // Map ProjectMember to ProjectMemberResponse
        List<ProjectMemberResponse> memberResponses = projectMembers.stream().map(member ->
                ProjectMemberResponse.builder()
                        .id(member.getUserId().longValue())
                        .name(member.getUsername())
                        .role(member.getRole())
                        .build()
        ).toList();

        // Build and return the ProjectResponse
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .startDate(project.getStartDate())
                .members(memberResponses)
                .build();
    }


}
