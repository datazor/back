package lm.mr.pmt.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class TaskHistory {
    private Long id;
    private Long taskId;
    private String modifiedField;
    private String oldValue;
    private String newValue;
    private Integer userId;
    private Date modificationDate;
}
