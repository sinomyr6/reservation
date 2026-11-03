
package restaurant.reservation.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restaurant.reservation.entity.Notification;
import restaurant.reservation.entity.NotificationType;
import restaurant.reservation.entity.Reservation;
import restaurant.reservation.entity.ReservationStatus;
import restaurant.reservation.exception.BusinessRuleException;
import restaurant.reservation.exception.ResourceNotFoundException;
import restaurant.reservation.repository.ReservationRepository;
import restaurant.reservation.service.NotificationService;
import restaurant.reservation.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository repository;
    private final NotificationService notificationService;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a");

    public ReservationServiceImpl(
            ReservationRepository repository,
            NotificationService notificationService) {

        this.repository = repository;
        this.notificationService = notificationService;
    }

    // CREATE RESERVATION
    @Override
    @Transactional
    public String createReservation(Reservation reservation) {

        LocalDateTime now = LocalDateTime.now();

        if (reservation.getReservationDate() == null
                || !reservation.getReservationDate().isAfter(now)) {
            throw new BusinessRuleException(
                    "Reservation date must be in the future.");
        }

        if (reservation.getNumberOfGuests() <= 0) {
            throw new BusinessRuleException(
                    "Number of guests must be greater than zero.");
        }

        String reservationId =
                "RSV" + UUID.randomUUID().toString().replace("-", "");

        reservation.setReservationId(reservationId);
        reservation.setStatus(ReservationStatus.PENDING);

        repository.save(reservation);

        String subject =
                "[" + reservationId + "] - New Reservation";

        String message =
                "Your reservation request has been received for "
                + reservation.getReservationDate().format(FORMATTER)
                + " for "
                + reservation.getNumberOfGuests()
                + " guests. Your reservation is pending confirmation.";

        sendNotification(
                subject,
                message,
                reservation.getEmail(),
                NotificationType.RESERVATION_CREATED,
                reservationId
        );

        return "Reservation created successfully. Awaiting confirmation.";
    }

    // CANCEL RESERVATION
    @Override
    @Transactional
    public String cancelReservation(String reservationId) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(
                existing,
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED
        );

        validateFutureReservation(existing);

        existing.setStatus(ReservationStatus.CANCELLED);

        String subject =
                "[" + reservationId + "] - Reservation Cancelled";

        String message =
                "Your reservation scheduled for "
                + existing.getReservationDate().format(FORMATTER)
                + " has been cancelled successfully.";

        sendNotification(
                subject,
                message,
                existing.getEmail(),
                NotificationType.RESERVATION_CANCELLED,
                reservationId
        );

        return "Reservation cancelled successfully.";
    }

    // GET RESERVATIONS BY CUSTOMER EMAIL
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getReservationByEmail(String email) {

        return repository.findReservationsByEmail(email);
    }

    // UPDATE RESERVATION
    @Override
    @Transactional
    public String updateReservation(
            String reservationId,
            LocalDateTime reservationDate,
            Integer numberOfGuests) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(
                existing,
                ReservationStatus.PENDING,
                ReservationStatus.CONFIRMED
        );

        validateFutureReservation(existing);

        LocalDateTime now = LocalDateTime.now();

        if (reservationDate == null || !reservationDate.isAfter(now)) {
            throw new BusinessRuleException(
                    "The new reservation date must be in the future.");
        }

        if (numberOfGuests == null || numberOfGuests <= 0) {
            throw new BusinessRuleException(
                    "Number of guests must be greater than zero.");
        }

        String oldDate =
                existing.getReservationDate().format(FORMATTER);

        int oldGuests = existing.getNumberOfGuests();

        String newDate = reservationDate.format(FORMATTER);

        existing.setReservationDate(reservationDate);
        existing.setNumberOfGuests(numberOfGuests);

        // Require restaurant confirmation again.
        existing.setStatus(ReservationStatus.PENDING);

        String subject =
                "[" + reservationId + "] - Reservation Updated";

        String message =
                "Your reservation has been updated. "
                + "Previous date: " + oldDate
                + ". New date: " + newDate
                + ". Previous guests: " + oldGuests
                + ". New guests: " + numberOfGuests
                + ". Your reservation is now pending confirmation.";

        sendNotification(
                subject,
                message,
                existing.getEmail(),
                NotificationType.RESERVATION_UPDATED,
                reservationId
        );

        return "Reservation updated successfully. Awaiting confirmation.";
    }

    // DELETE RESERVATION
    // Restrict this operation to authorized administrators.
    @Override
    @Transactional
    public String deleteReservation(String reservationId) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(
                existing,
                ReservationStatus.CANCELLED,
                ReservationStatus.COMPLETED
        );

        repository.delete(existing);

        return "Reservation deleted successfully.";
    }

    // CONFIRM RESERVATION
    @Override
    @Transactional
    public String confirmReservation(String reservationId) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(existing, ReservationStatus.PENDING);
        validateFutureReservation(existing);

        existing.setStatus(ReservationStatus.CONFIRMED);

        String subject =
                "[" + reservationId + "] - Reservation Confirmed";

        String message =
                "Your reservation has been confirmed by the restaurant "
                + "for "
                + existing.getReservationDate().format(FORMATTER)
                + " for "
                + existing.getNumberOfGuests()
                + " guests.";

        sendNotification(
                subject,
                message,
                existing.getEmail(),
                NotificationType.RESERVATION_CONFIRMED,
                reservationId
        );

        return "Reservation confirmed successfully.";
    }

    // START RESERVATION
    @Override
    @Transactional
    public String startReservation(String reservationId) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(existing, ReservationStatus.CONFIRMED);

        existing.setStatus(ReservationStatus.ONGOING);

        return "Reservation is now ongoing.";
    }

    // COMPLETE RESERVATION
    @Override
    @Transactional
    public String completeReservation(String reservationId) {

        Reservation existing = findReservationOrThrow(reservationId);

        validateStatus(existing, ReservationStatus.ONGOING);

        existing.setStatus(ReservationStatus.COMPLETED);

        return "Reservation completed successfully.";
    }

    // GET CONFIRMED RESERVATIONS BY CUSTOMER EMAIL
    @Override
    @Transactional(readOnly = true)
    public List<Reservation> getConfirmedReservationByEmail(String email) {

        return repository.findConfirmedReservationsByEmail(
                email,
                ReservationStatus.CONFIRMED
        );
    }

    // REUSABLE RESERVATION LOOKUP
    private Reservation findReservationOrThrow(String reservationId) {

        return repository.findById(reservationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found: " + reservationId));
    }

    // REUSABLE STATUS VALIDATION
    private void validateStatus(
            Reservation reservation,
            ReservationStatus... allowedStatuses) {

        for (ReservationStatus status : allowedStatuses) {

            if (reservation.getStatus() == status) {
                return;
            }
        }

        throw new BusinessRuleException(
                "Invalid reservation status: " + reservation.getStatus());
    }

    // REUSABLE FUTURE RESERVATION VALIDATION
    private void validateFutureReservation(Reservation reservation) {

        if (reservation.getReservationDate() == null
                || !reservation.getReservationDate()
                .isAfter(LocalDateTime.now())) {

            throw new BusinessRuleException(
                    "This operation is not allowed for past or started reservations.");
        }
    }

    // REUSABLE NOTIFICATION METHOD
    private void sendNotification(
            String subject,
            String message,
            String customerEmail,
            NotificationType notificationType,
            String reservationId) {

        Notification notification = new Notification();

        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setCustomerEmail(customerEmail);
        notification.setNotificationType(notificationType);
        notification.setReservationId(reservationId);

        notificationService.sendNotification(notification);
    }
}
