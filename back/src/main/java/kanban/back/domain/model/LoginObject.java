package kanban.back.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginObject {
    private String token;
    private User user;
}
