
package restaurant.reservation.service;

import java.util.List;

import restaurant.reservation.entity.Notification;

public interface NotificationService {

    Notification sendNotification(Notification notification);

    List<Notification> getNotificationByEmail(String email);
}
