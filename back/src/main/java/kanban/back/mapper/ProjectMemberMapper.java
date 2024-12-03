package kanban.back.mapper;

import kanban.back.domain.model.ProjectMember;
import kanban.back.port.out.repository.ProjectMemberEntity;



public class ProjectMemberMapper {


    public static ProjectMember toDomain(ProjectMemberEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProjectMember.builder()
                .projectId(entity.getProject().getId())
                .userId(entity.getUser().getId())
                .username(entity.getUser().getUsername())
                .role(entity.getRole())
                .membership(entity.getMembership())
                .build();
    }
}

