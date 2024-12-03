package lm.mr.pmt.mapper;

import lm.mr.pmt.controller.projectManagement.response.TaskHistoryResponse;
import lm.mr.pmt.domain.model.Change;
import lm.mr.pmt.domain.model.TaskHistory;
import lm.mr.pmt.port.out.repository.TaskHistoryEntity;

import java.util.ArrayList;
import java.util.List;

public class TaskHistoryMapper {

    public static TaskHistoryEntity toEntity(TaskHistory taskHistory) {
        TaskHistoryEntity entity = new TaskHistoryEntity();
        entity.setUserId(taskHistory.getUserId().longValue());
        entity.setTaskId(taskHistory.getTaskId());
        entity.setModifiedField(taskHistory.getModifiedField());
        entity.setOldValue(taskHistory.getOldValue());
        entity.setNewValue(taskHistory.getNewValue());
        entity.setModificationDate(taskHistory.getModificationDate());
        return entity;
    }

    public static TaskHistoryResponse toResponse(TaskHistoryEntity entity) {
        var change = Change.builder()
                .field(entity.getModifiedField())
                .oldValue(entity.getOldValue())
                .newValue(entity.getNewValue())
                .build();

        List<Change> changes = new ArrayList<>();
        changes.add(change);

        return TaskHistoryResponse.builder()
                .taskId(entity.getTaskId())
                .updatedBy(entity.getUserId().toString())
                .updatedAt(entity.getModificationDate())
                .changes(changes)
                .build();
    }
}
