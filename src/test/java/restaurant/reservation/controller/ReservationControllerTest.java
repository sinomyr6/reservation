
package restaurant.reservation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import restaurant.reservation.entity.Reservation;
import restaurant.reservation.service.ReservationService;

class ReservationControllerTest {

	@Mock
	private ReservationService reservationService;

	@InjectMocks
	private ReservationController reservationController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getAllReservationByEmail_shouldReturnReservations() {

		String email = "customer@example.com";
		List<Reservation> reservations = List.of();

		when(reservationService.getReservationByEmail(email))
		.thenReturn(reservations);

		ResponseEntity<List<Reservation>> response =
				reservationController.getAllReservationsByEmail(email);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(reservations, response.getBody());

		verify(reservationService).getReservationByEmail(email);
	}

	@Test
	void getUpcomingReservationByEmail_shouldReturnReservations() {

		String email = "customer@example.com";
		List<Reservation> reservations = List.of();

		when(reservationService.getConfirmedReservationByEmail(email))
		.thenReturn(reservations);

		ResponseEntity<List<Reservation>> response =
				reservationController.getUpcomingReservationsByEmail(email);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(reservations, response.getBody());

		verify(reservationService)
		.getConfirmedReservationByEmail(email);
	}

	@Test
	void cancelReservation_shouldReturnSuccessMessage() {

		String reservationId = "RSV123";
		String expected = "Reservation cancelled successfully";

		when(reservationService.cancelReservation(reservationId))
		.thenReturn(expected);

		ResponseEntity<String> response =
				reservationController.cancelReservation(reservationId);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(expected, response.getBody());

		verify(reservationService).cancelReservation(reservationId);
	}

	@Test
	void confirmReservation_shouldReturnSuccessMessage() {

		String reservationId = "RSV123";
		String expected = "Reservation confirmed successfully";

		when(reservationService.confirmReservation(reservationId))
		.thenReturn(expected);

		ResponseEntity<String> response =
				reservationController.confirmReservation(reservationId);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(expected, response.getBody());

		verify(reservationService).confirmReservation(reservationId);
	}

	@Test
	void startReservation_shouldReturnSuccessMessage() {

		String reservationId = "RSV123";
		String expected = "Reservation started successfully";

		when(reservationService.startReservation(reservationId))
		.thenReturn(expected);

		ResponseEntity<String> response =
				reservationController.startReservation(reservationId);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(expected, response.getBody());

		verify(reservationService).startReservation(reservationId);
	}

	@Test
	void completeReservation_shouldReturnSuccessMessage() {

		String reservationId = "RSV123";
		String expected = "Reservation completed successfully";

		when(reservationService.completeReservation(reservationId))
		.thenReturn(expected);

		ResponseEntity<String> response =
				reservationController.completeReservation(reservationId);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(expected, response.getBody());

		verify(reservationService).completeReservation(reservationId);
	}


	@Test
	void deleteReservation_shouldReturnNoContent() {

		String reservationId = "RSV123";

		ResponseEntity<Void> response =
				reservationController.deleteReservation(reservationId);

		assertEquals(204, response.getStatusCode().value());

		assertNull(response.getBody());

		verify(reservationService).deleteReservation(reservationId);
	}

}
