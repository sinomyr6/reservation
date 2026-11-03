
package restaurant.reservation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import restaurant.reservation.entity.Notification;
import restaurant.reservation.service.NotificationService;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllNotificationsByEmail_shouldReturnNotifications() {

        // Arrange
        String email = "customer@example.com";

        List<Notification> notifications = List.of(
                new Notification(),
                new Notification()
        );

        when(notificationService.getNotificationByEmail(email))
                .thenReturn(notifications);

        // Act
        ResponseEntity<List<Notification>> response =
                notificationController.getAllNotificationsByEmail(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(notifications, response.getBody());

        verify(notificationService).getNotificationByEmail(email);
    }
}
