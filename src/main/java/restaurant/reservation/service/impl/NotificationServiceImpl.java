
package restaurant.reservation.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restaurant.reservation.entity.Notification;
import restaurant.reservation.repository.NotificationRepository;
import restaurant.reservation.service.NotificationService;
import restaurant.reservation.exception.BusinessRuleException;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    // CREATE NOTIFICATION
    @Override
    @Transactional
    public Notification sendNotification(Notification notification) {

        if (notification == null) {
            throw new BusinessRuleException(
                    "Notification cannot be null.");
        }

        if (notification.getCustomerEmail() == null
                || notification.getCustomerEmail().isBlank()) {

            throw new BusinessRuleException(
                    "Customer email is required.");
        }

        String notificationId =
                "NTF" + UUID.randomUUID().toString().replace("-", "");

        notification.setNotificationId(notificationId);
        notification.setDateSent(LocalDateTime.now());
        notification.setMarkRead(false);

        return notificationRepository.save(notification);
    }

    // GET NOTIFICATIONS BY CUSTOMER EMAIL
    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationByEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new BusinessRuleException(
                    "Customer email is required.");
        }

        return notificationRepository.findNotificationsByEmail(email);
    }
}
