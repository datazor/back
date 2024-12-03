package lm.mr.pmt.controller.projectManagement.request;

import lm.mr.pmt.domain.model.TaskStatus;
import lm.mr.pmt.domain.model.TaskPriority;
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
