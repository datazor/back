package kanban.back.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Change {
    private String field;
    private Object oldValue;
    private Object newValue;
}