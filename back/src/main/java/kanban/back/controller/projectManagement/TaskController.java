package kanban.back.controller.projectManagement;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kanban.back.domain.model.Task;
import kanban.back.controller.projectManagement.request.TaskRequest;
import kanban.back.controller.projectManagement.request.TaskUpdateRequest;
import kanban.back.controller.projectManagement.response.TaskHistoryResponse;
import kanban.back.controller.projectManagement.response.TaskResponse;
import kanban.back.mapper.TaskMapper;
import kanban.back.port.out.repository.TaskPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskPort taskPort;


    @PostMapping("/create")
    @Operation(summary = "Create a Task", description = "Creates a new task within a specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created the task"),
            @ApiResponse(responseCode = "400", description = "Invalid task data or project ID"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        Task createdTask = taskPort.createTask(TaskMapper.toDomain(taskRequest));
        return TaskMapper.toResponse(createdTask);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get Tasks for Project", description = "Fetches all tasks associated with a specific project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<TaskResponse> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskPort.getTasksByProjectId(projectId);
        return tasks.stream().map(TaskMapper::toResponse).toList();
    }

    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskHistoryResponse>> getTaskHistory(@PathVariable Long taskId) {
        List<TaskHistoryResponse> historyResponses = taskPort.getTaskHistoryByTaskId(taskId);
        return ResponseEntity.ok(historyResponses);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update Task", description = "Updates the details of an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @RequestBody TaskUpdateRequest request) {
        var updatedTask = taskPort.updateTask(taskId, request);
        var response = TaskMapper.toResponse(updatedTask);
        return ResponseEntity.ok(response);
    }
}
