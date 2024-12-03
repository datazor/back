package kanban.back.port.out;


import jakarta.transaction.Transactional;
import kanban.back.controller.exception.InvalidRoleException;
import kanban.back.controller.exception.ProjectNotFoundException;
import kanban.back.controller.exception.ResourceNotFoundException;
import kanban.back.controller.projectManagement.request.InvitationRequest;
import kanban.back.domain.model.Project;
import kanban.back.domain.model.ProjectMember;
import kanban.back.mapper.ProjectMemberMapper;
import kanban.back.port.out.repository.*;
import kanban.back.service.userManagement.userDetails.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectJPAPort implements ProjectPort {


    private ProjectRepository projectRepository;

    private ProjectMemberRepository projectMemberRepository;

    private UserRepository userRepository;

    private ApplicationEventPublisher eventPublisher;



    @Override
    public Project create(Project project) {
        var projectEntity = toEntity(project);
        var savedProjectEntity = projectRepository.save(projectEntity);
        var email = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var userEntity = userRepository.findByEmail(email);
        projectMemberRepository.save(new ProjectMemberEntity(projectEntity, userEntity, Role.Administrator, Membership.Enacted));
        return toDomain(savedProjectEntity);
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getByName(String name) {
        return projectRepository.findByName(name)
                                .orElseThrow()
                                .stream().map(this::toDomain)
                                .toList();

    }

    @Override
    public List<ProjectMember> getMembers(Project project) {
        return projectMemberRepository.findByProject_Id(project.getId())
                .orElseThrow()
                .stream().map(ProjectMemberMapper::toDomain)
                .filter(projectMember -> projectMember.getMembership() == Membership.Enacted)
                .toList();
    }

    @Override
    public Project getById(Integer id) {
        return toDomain(projectRepository.findById(id).orElseThrow());
    }


    @Override
    public List<Project> getProjectByUsername(String username) {
        // Fetch the user by username
        var user = userRepository.findByUsername(username);

        // Find all project memberships for the user
        var projectMemberships = projectMemberRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("No project memberships found for user"));

        // Use getById to retrieve each project and collect them into a list
        return projectMemberships.stream()
                .map(projectMemberEntity -> getById(Math.toIntExact(projectMemberEntity.getProjectId())))
                .collect(Collectors.toList());
    }

    @Override
    public void changeUserRole(Long projectId, Long userId, Role newRole) {
        var projectMemberEntity = projectMemberRepository.findByProject_IdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException("Project or user not found"));

        if (newRole == null) {
            throw new InvalidRoleException("Invalid role specified");
        }

        projectMemberEntity.setRole(newRole);
        projectMemberRepository.save(projectMemberEntity);
    }

    @Override
    @Transactional
    public void inviteUser(InvitationRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail());

        ProjectEntity project = projectRepository.findById(Math.toIntExact(request.getProjectId()))
                .orElseThrow(() -> new ResourceNotFoundException("Project with ID " + request.getProjectId() + " not found"));

        boolean alreadyExists = projectMemberRepository.existsById(new ProjectMemberId(project.getId(), user.getId()));

        if (!alreadyExists) {
            ProjectMemberEntity newMember = new ProjectMemberEntity(project, user, Role.valueOf(request.getRole()), Membership.Pending);
            projectMemberRepository.save(newMember);
        }
        eventPublisher.publishEvent(
                ProjectInvitation.builder()
                        .projectId(project.getId())
                        .invitedBy(request.getInvitedById())
                        .projectName(project.getName())
                        .username(user.getUsername())
                        .build()
        );
    }

    @Override
    @Transactional
    public void respondToInvitation(Long projectId, boolean accept, String username) {
        // Fetch the userId from the UserRepository
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new IllegalArgumentException("User with username " + username + " not found");
        }
        Integer userId = userEntity.getId();

        // Create composite key to find the ProjectMemberEntity
        ProjectMemberId memberId = new ProjectMemberId(projectId, userId);

        // Fetch the ProjectMemberEntity
        ProjectMemberEntity projectMember = projectMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Project membership not found"));

        if (accept) {
            // If accepted, set membership to ENACTED
            projectMember.setMembership(Membership.Enacted);
            projectMemberRepository.save(projectMember);
        } else {
            // If declined, delete the ProjectMemberEntity
            projectMemberRepository.delete(projectMember);
        }
    }

    private ProjectEntity toEntity(Project project) {
        ProjectEntity entity = new ProjectEntity();
        entity.setName(project.getName());
        entity.setDescription(project.getDescription());
        entity.setStartDate(project.getStartDate());
        return entity;
    }

    private Project toDomain(ProjectEntity entity) {
        return Project.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .build();
    }
}
