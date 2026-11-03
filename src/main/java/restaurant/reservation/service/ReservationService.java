
package restaurant.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import restaurant.reservation.entity.Reservation;

public interface ReservationService {

    String createReservation(Reservation reservation);

    String cancelReservation(String reservationId);

    List<Reservation> getReservationByEmail(String email);
    
    List<Reservation> getConfirmedReservationByEmail(String email);


	String updateReservation(
	    String reservationId,
	    LocalDateTime reservationDate,
	    Integer numberOfGuests
	);


    String deleteReservation(String reservationId);

    String confirmReservation(String reservationId);

    String startReservation(String reservationId);

    String completeReservation(String reservationId);
}
