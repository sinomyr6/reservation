package restaurant.reservation.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import restaurant.reservation.entity.Notification;
import restaurant.reservation.entity.Reservation;
import restaurant.reservation.entity.ReservationStatus;
import restaurant.reservation.repository.NotificationRepository;
import restaurant.reservation.repository.ReservationRepository;
import restaurant.reservation.entity.NotificationType;

@Component
public class ReservationReminderScheduler {

	private static final NotificationType REMINDER_TYPE =
	        NotificationType.RESERVATION_REMINDER;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");

    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;

    public ReservationReminderScheduler(
            ReservationRepository reservationRepository,
            NotificationRepository notificationRepository) {

        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(fixedRate = 60000)
//    @Scheduled(fixedRate = 5000) // for testing
    @Transactional
    public void createReservationReminders() {

        LocalDateTime now = LocalDateTime.now();

        // Find reservations whose reminder time falls
        // within the next one-minute scheduling window.
        LocalDateTime start = now.plusHours(4);
        LocalDateTime end = start.plusMinutes(1);
        
        // for testing
        // TEMPORARY TEST: find reservations 1–2 minutes from now
//        LocalDateTime start = now.plusMinutes(1);
//        LocalDateTime end = now.plusMinutes(2);

        List<Reservation> reservations =
                reservationRepository
                        .findByStatusAndReservationDateBetween(
                                ReservationStatus.CONFIRMED,
                                start,
                                end);

        for (Reservation reservation : reservations) {

            // Do not create duplicate reminder records.
            boolean reminderExists =
                    notificationRepository
                            .existsByReservationIdAndNotificationType(
                                    reservation.getReservationId(),
                                    REMINDER_TYPE);

            if (reminderExists) {
                continue;
            }

            Notification notification = new Notification();

            notification.setNotificationId(
                    "NTF" + java.util.UUID.randomUUID());

            notification.setReservationId(
                    reservation.getReservationId());

            notification.setNotificationType(REMINDER_TYPE);

            notification.setCustomerEmail(reservation.getEmail());

            notification.setSubject(
                    "[" + reservation.getReservationId()
                    + "] - Reservation Reminder");

            notification.setMessage(
                    "Reminder: Your restaurant reservation is scheduled for "
                    + reservation.getReservationDate().format(FORMATTER)
                    + " for "
                    + reservation.getNumberOfGuests()
                    + " guests.");

            notification.setDateSent(LocalDateTime.now());

            notification.setMarkRead(false);

            notificationRepository.save(notification);
        }
    }
}
