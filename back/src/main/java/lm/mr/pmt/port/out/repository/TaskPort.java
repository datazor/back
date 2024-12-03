package lm.mr.pmt.port.out.repository;

import lm.mr.pmt.controller.projectManagement.request.TaskUpdateRequest;
import lm.mr.pmt.controller.projectManagement.response.TaskHistoryResponse;
import lm.mr.pmt.domain.model.Task;

import java.util.List;

public interface TaskPort {
    Task createTask(Task task);

    List<Task> getTasksByProjectId(Long projectId);

    Task updateTask(Long taskId, TaskUpdateRequest request);

    List<TaskHistoryResponse> getTaskHistoryByTaskId(Long taskId);
}
