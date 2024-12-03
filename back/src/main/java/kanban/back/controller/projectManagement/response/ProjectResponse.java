package kanban.back.controller.projectManagement.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private List<ProjectMemberResponse> members;
}

