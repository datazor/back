package kanban.back.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Builder
@Getter
@Setter
public class Project {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
}
