package lm.mr.pmt.controller.projectManagement.request;

import lm.mr.pmt.domain.model.TaskPriority;
import lm.mr.pmt.domain.model.TaskStatus;
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
