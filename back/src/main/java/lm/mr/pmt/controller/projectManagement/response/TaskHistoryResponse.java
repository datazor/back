package lm.mr.pmt.controller.projectManagement.response;


import lm.mr.pmt.domain.model.Change;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class TaskHistoryResponse {
    private Long taskId;
    private String updatedBy;
    private Date updatedAt;
    private List<Change> changes;
}
