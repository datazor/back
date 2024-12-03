package kanban.back.controller.projectManagement.request;

import kanban.back.domain.model.TaskStatus;
import kanban.back.domain.model.TaskPriority;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Date dueDate;
    private Long assigneeId;
    private Long projectId;
}
