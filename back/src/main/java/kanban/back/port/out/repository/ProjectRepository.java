package kanban.back.port.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public  interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    Optional<List<ProjectEntity>> findByName(String name);
}
