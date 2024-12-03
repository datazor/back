package kanban.back.port.out.repository;

import kanban.back.controller.projectManagement.request.TaskUpdateRequest;
import kanban.back.controller.projectManagement.response.TaskHistoryResponse;
import kanban.back.domain.model.Task;

import java.util.List;

public interface TaskPort {
    Task createTask(Task task);

    List<Task> getTasksByProjectId(Long projectId);

    Task updateTask(Long taskId, TaskUpdateRequest request);

    List<TaskHistoryResponse> getTaskHistoryByTaskId(Long taskId);
}
