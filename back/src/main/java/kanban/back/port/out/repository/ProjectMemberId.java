package kanban.back.port.out.repository;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberId implements Serializable {

    private Long projectId;
    private Integer userId;
}
