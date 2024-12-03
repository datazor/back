package kanban.back.mapper;

import kanban.back.controller.projectManagement.request.TaskRequest;
import kanban.back.controller.projectManagement.response.TaskResponse;
import kanban.back.domain.model.Task;
import kanban.back.port.out.repository.TaskEntity;

import java.util.Date;

public class TaskMapper {
    public static TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setName(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setStatus(task.getStatus());
        entity.setPriority(task.getPriority());
        entity.setCreationDate(task.getCreationDate());
        entity.setDueDate(task.getDueDate());
        entity.setCompletionDate(task.getCompletionDate());
        entity.setProjectId(task.getProjectId());
        entity.setAssignedTo(task.getAssignedTo());
        return entity;
    }

    public static Task toDomain(TaskEntity entity) {
        Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getName());
        task.setDescription(entity.getDescription());
        task.setStatus(entity.getStatus());
        task.setPriority(entity.getPriority());
        task.setCreationDate(entity.getCreationDate());
        task.setDueDate(entity.getDueDate());
        task.setCompletionDate(entity.getCompletionDate());
        task.setProjectId(entity.getProjectId());
        task.setAssignedTo(entity.getAssignedTo());
        return task;
    }

    public static TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setCreatedAt(task.getCreationDate());
        response.setDueDate(task.getDueDate());
        response.setCompletionDate(task.getCompletionDate());
        response.setProjectId(task.getProjectId());
        response.setAssigneeId(task.getAssignedTo());
        return response;
    }

    public static Task toDomain(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setAssignedTo(request.getAssigneeId());
        task.setProjectId(request.getProjectId());
        task.setCreationDate(new Date());
        return task;
    }

}
