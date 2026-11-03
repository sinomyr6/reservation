
package restaurant.reservation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    private String notificationId;

    private String reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private String customerEmail;

    private String subject;

    private String message;

    private LocalDateTime dateSent;

    private Boolean markRead;
}
