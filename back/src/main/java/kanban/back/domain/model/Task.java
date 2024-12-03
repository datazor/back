package kanban.back.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Date creationDate;
    private Date dueDate;
    private Date completionDate;
    private Long projectId;
    private Long assignedTo;
}
