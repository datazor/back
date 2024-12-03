package lm.mr.pmt.port.out.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Task_History")
public class TaskHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Task_ID", nullable = false)
    private Long taskId;

    @Column(name = "Modified_field", nullable = false)
    private String modifiedField;

    @Column(name = "Old_value")
    private String oldValue;

    @Column(name = "New_value")
    private String newValue;

    @Column(name = "User_ID", nullable = false)
    private Long userId;

    @Column(name = "Modification_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate = new Date();
}
