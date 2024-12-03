package lm.mr.pmt.service.listener;

import lm.mr.pmt.port.out.repository.ProjectInvitation;
import lm.mr.pmt.service.projectManagement.NotificationService;
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
