package lm.mr.pmt.port.out.repository;


import lm.mr.pmt.controller.exception.InvalidTaskDataException;
import lm.mr.pmt.controller.exception.ProjectNotFoundException;
import lm.mr.pmt.controller.exception.TaskNotFoundException;
import lm.mr.pmt.controller.projectManagement.request.TaskUpdateRequest;
import lm.mr.pmt.controller.projectManagement.response.TaskHistoryResponse;
import lm.mr.pmt.domain.model.Task;
import lm.mr.pmt.domain.model.TaskHistory;
import lm.mr.pmt.mapper.TaskHistoryMapper;
import lm.mr.pmt.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskJPAPort implements TaskPort {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskJPAPort(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task createTask(Task task) {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new InvalidTaskDataException("Task name is required");
        }
        if (!projectRepository.existsById(Math.toIntExact(task.getProjectId()))) {
            throw new ProjectNotFoundException("Project with ID " + task.getProjectId() + " not found");
        }

        var taskEntity = TaskMapper.toEntity(task);
        var savedTaskEntity = taskRepository.save(taskEntity);
        return TaskMapper.toDomain(savedTaskEntity);
    }

    @Override
    public List<Task> getTasksByProjectId(Long projectId) {
        var tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(TaskMapper::toDomain)
                .toList();
    }


    @Override
    public Task updateTask(Long taskId, TaskUpdateRequest request) {
        return taskRepository.findById(taskId).map(task -> {
            List<TaskHistory> historyEntries = new ArrayList<>();

            if (request.getTitle() != null && !request.getTitle().equals(task.getName())) {
                historyEntries.add(createHistory(taskId, "Name", task.getName(), request.getTitle(), request.getUpdater()));
                task.setName(request.getTitle());
            }
            if (request.getDescription() != null && !request.getDescription().equals(task.getDescription())) {
                historyEntries.add(createHistory(taskId, "Description", task.getDescription(), request.getDescription(), request.getUpdater()));
                task.setDescription(request.getDescription());
            }
            if (request.getStatus() != null && !request.getStatus().equals(task.getStatus())) {
                historyEntries.add(createHistory(taskId, "Status", task.getStatus().name(), request.getStatus().name(), request.getUpdater()));
                task.setStatus(request.getStatus());
            }
            if (request.getPriority() != null && !request.getPriority().equals(task.getPriority())) {
                historyEntries.add(createHistory(taskId, "Priority", task.getPriority().name(), request.getPriority().name(), request.getUpdater()));
                task.setPriority(request.getPriority());
            }
            if (request.getAssigneeId() != null && !request.getAssigneeId().equals(task.getAssignedTo())) {
                historyEntries.add(createHistory(taskId, "AssignedTo", String.valueOf(task.getAssignedTo()), String.valueOf(request.getAssigneeId()), request.getUpdater()));
                task.setAssignedTo(request.getAssigneeId());
            }

            taskRepository.save(task);
            saveHistoryEntries(historyEntries);

            return TaskMapper.toDomain(task);
        }).orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));
    }

    private TaskHistory createHistory(Long taskId, String field, String oldValue, String newValue, String username) {
        var user = userRepository.findByUsername(username);

        if (field.equals("AssignedTo")) {
            oldValue = oldValue.equals("null")  ? " " : userRepository.findById(Integer.valueOf(oldValue)).get().getUsername();
            newValue = newValue.equals("null") ? " " : userRepository.findById(Integer.valueOf(newValue)).get().getUsername();
        }

        return TaskHistory.builder()
                .taskId(taskId)
                .modifiedField(field)
                .oldValue(oldValue)
                .newValue(newValue)
                .userId(user.getId())
                .modificationDate(new java.util.Date())
                .build();
    }

    private void saveHistoryEntries(List<TaskHistory> historyEntries) {
        List<TaskHistoryEntity> entities = historyEntries.stream()
                .map(TaskHistoryMapper::toEntity)
                .toList();
        taskHistoryRepository.saveAll(entities);
    }

    @Override
    public List<TaskHistoryResponse> getTaskHistoryByTaskId(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }

        return  taskHistoryRepository.findByTaskId(taskId).stream()
                .map(TaskHistoryMapper::toResponse)
                .toList();
    }

}

