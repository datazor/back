package kanban.back.port.out.repository;

import jakarta.persistence.*;
import kanban.back.domain.model.TaskPriority;
import kanban.back.domain.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate;

    private Date dueDate;
    private Date completionDate;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "assigned_to")
    private Long assignedTo;
}
