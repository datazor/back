package kanban.back.port.out.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, ProjectMemberId> {
    Optional<List<ProjectMemberEntity>> findByProject_Id(Long projectId);

    Optional<List<ProjectMemberEntity>> findByUser_Id(Integer userId);

    Optional<ProjectMemberEntity> findByProject_IdAndUser_Id(Long projectId, Long userId);
}
