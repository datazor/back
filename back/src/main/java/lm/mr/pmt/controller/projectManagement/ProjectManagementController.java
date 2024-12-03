package lm.mr.pmt.controller.projectManagement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lm.mr.pmt.controller.projectManagement.request.ProjectCreateRequest;
import lm.mr.pmt.controller.projectManagement.response.ProjectResponse;
import lm.mr.pmt.domain.model.Project;
import lm.mr.pmt.service.projectManagement.ProjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lm.mr.pmt.mapper.ProjectMapper.toResponse;
import static lm.mr.pmt.mapper.ProjectMapper.toResponseList;

@Validated
@RestController
@RequestMapping("api/v1/projects")
@Tag(name = "Project Management", description = "Operations for managing projects, including creation and retrieval.")
public class ProjectManagementController {

    @Autowired
    private ProjectManagementService projectManagementService;

    @Operation(
            summary = "Create a new project",
            description = "Creates a new project with a name, description, and start date.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Project created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid project data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/create")
    public Project createProject(@Valid @RequestBody ProjectCreateRequest request) {
        return projectManagementService.createProject(request);
    }

    @Operation(
            summary = "Get a project by ID",
            description = "Retrieves a project by its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Project not found")
            }
    )
    @GetMapping("/project/{id}")
    public ProjectResponse getProjectById(@PathVariable Integer id) {
        return toResponse(projectManagementService.getProjectById(id));
    }

    @Operation(
            summary = "List all projects",
            description = "Retrieves a list of all projects.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{username}")
    public List<ProjectResponse> getProjectsByUsername(@PathVariable String username) {
        return toResponseList(projectManagementService.getProjectByUsername(username));
    }




}

