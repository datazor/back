package lm.mr.pmt.port.out.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Table(name = "Project_Member")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberEntity implements Serializable {

    @EmbeddedId
    private ProjectMemberId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("projectId")
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PROJECT_MEMBER_PROJECT"))
    private ProjectEntity project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PROJECT_MEMBER_USER"))
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Membership membership;

    public ProjectMemberEntity(ProjectEntity projectEntity, UserEntity userEntity, Role role, Membership membership) {
        this.id = new ProjectMemberId(projectEntity.getId(), userEntity.getId());
        this.project = projectEntity;
        this.user = userEntity;
        this.role = role;
        this.membership = membership;
    }


    public Long getProjectId() {
        return project.getId();
    }
}
