package kanban.back.service.listener;

import kanban.back.service.projectManagement.NotificationService;
import kanban.back.port.out.repository.ProjectInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectInvitationListener {

    private final NotificationService notificationService;

    @Autowired
    public ProjectInvitationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handleUserInvitedEvent(ProjectInvitation event) {
        System.out.print("triggered");
        notificationService.sendInvitation(event);
    }
}
