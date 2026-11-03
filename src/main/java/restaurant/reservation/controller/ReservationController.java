
package restaurant.reservation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import restaurant.reservation.entity.Reservation;
import restaurant.reservation.service.ReservationService;
import restaurant.reservation.dto.UpdateReservationRequest;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // GET: Retrieve all reservations by customer email
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservationsByEmail(
            @RequestParam String email) {

        List<Reservation> reservations =
                reservationService.getReservationByEmail(email);

        return ResponseEntity.ok(reservations);
    }

    // GET: Retrieve upcoming/confirmed reservations by customer email
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservationsByEmail(
            @RequestParam String email) {

        List<Reservation> reservations =
                reservationService.getConfirmedReservationByEmail(email);

        return ResponseEntity.ok(reservations);
    }

    // POST: Create a new reservation
    @PostMapping
    public ResponseEntity<String> createReservation(
            @Valid @RequestBody Reservation reservation) {

        String response =
                reservationService.createReservation(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PATCH: Update reservation details
    @PatchMapping("/{reservationId}")
    public ResponseEntity<String> updateReservation(
            @PathVariable String reservationId,
            @Valid @RequestBody UpdateReservationRequest request) {

        String response = reservationService.updateReservation(
                reservationId,
                request.getReservationDate(),
                request.getNumberOfGuests()
        );

        return ResponseEntity.ok(response);
    }

    // PATCH: Cancel a reservation
    @PatchMapping("/{reservationId}/cancellation")
    public ResponseEntity<String> cancelReservation(
            @PathVariable String reservationId) {

        String response =
                reservationService.cancelReservation(reservationId);

        return ResponseEntity.ok(response);
    }

    // DELETE: Permanently delete a reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable String reservationId) {

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.noContent().build();
    }

    // PATCH: Confirm a pending reservation
    @PatchMapping("/{reservationId}/confirmation")
    public ResponseEntity<String> confirmReservation(
            @PathVariable String reservationId) {

        String response =
                reservationService.confirmReservation(reservationId);

        return ResponseEntity.ok(response);
    }

    // PATCH: Start a confirmed reservation
    @PatchMapping("/{reservationId}/start")
    public ResponseEntity<String> startReservation(
            @PathVariable String reservationId) {

        String response =
                reservationService.startReservation(reservationId);

        return ResponseEntity.ok(response);
    }

    // PATCH: Complete an ongoing reservation
    @PatchMapping("/{reservationId}/completion")
    public ResponseEntity<String> completeReservation(
            @PathVariable String reservationId) {

        String response =
                reservationService.completeReservation(reservationId);

        return ResponseEntity.ok(response);
    }
}
