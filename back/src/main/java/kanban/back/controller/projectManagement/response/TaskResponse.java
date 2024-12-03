package kanban.back.controller.projectManagement.response;

import kanban.back.domain.model.TaskStatus;
import kanban.back.domain.model.TaskPriority;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Date createdAt;
    private Date dueDate;
    private Date completionDate;
    private Long projectId;
    private Long assigneeId;
}
