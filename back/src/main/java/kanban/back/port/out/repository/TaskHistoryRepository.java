package kanban.back.port.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Long> {

    List<TaskHistoryEntity> findByTaskId(Long taskId);
}
