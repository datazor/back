package kanban.back.controller.projectManagement.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespondRequest {

    private Long projectId;
    private boolean accept;
    private String username;
}
