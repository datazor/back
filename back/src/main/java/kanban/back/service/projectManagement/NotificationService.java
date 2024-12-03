package kanban.back.service.projectManagement;

import kanban.back.port.out.repository.ProjectInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;


    public void sendInvitation(ProjectInvitation event) {
        messagingTemplate.convertAndSendToUser(event.getUsername(), "/notification", event);
    }
}
