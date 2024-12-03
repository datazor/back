package kanban.back.controller.projectManagement.request;

import kanban.back.domain.model.TaskPriority;
import kanban.back.domain.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdateRequest {
    private String updater;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;

}
