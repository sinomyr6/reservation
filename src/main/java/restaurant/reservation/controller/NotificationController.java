
package restaurant.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import restaurant.reservation.entity.Notification;
import restaurant.reservation.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // Constructor-based Dependency Injection
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET /api/notifications/customer/{email}
    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Notification>> getAllNotificationsByEmail(
            @PathVariable String email) {

        List<Notification> notifications =
                notificationService.getNotificationByEmail(email);

        return ResponseEntity.ok(notifications);
    }
}
