
package restaurant.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import restaurant.reservation.entity.Notification;
import restaurant.reservation.entity.NotificationType;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query("SELECT n FROM Notification n WHERE n.customerEmail = :email")
    List<Notification> findNotificationsByEmail(
        @Param("email") String email
    );

    @Query("""
        SELECT CASE WHEN COUNT(n) > 0 THEN TRUE ELSE FALSE END
        FROM Notification n
        WHERE n.reservationId = :reservationId
          AND n.notificationType = :notificationType
        """)
    boolean existsByReservationIdAndNotificationType(
        @Param("reservationId") String reservationId,
        @Param("notificationType") NotificationType notificationType
    );
}
