
package restaurant.reservation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

	@Id
	private String reservationId;

	@NotBlank(message = "First name is required.")
	@Size(max = 50, message = "First name must not exceed 50 characters.")
	@Pattern(
			regexp = "^[\\p{L}][\\p{L} .'-]*$",
			message = "First name contains invalid characters."
			)
	@Column(nullable = false, length = 50)
	private String firstName;

	@NotBlank(message = "Last name is required.")
	@Size(max = 50, message = "Last name must not exceed 50 characters.")
	@Pattern(
			regexp = "^[\\p{L}][\\p{L} .'-]*$",
			message = "Last name contains invalid characters."
			)
	@Column(nullable = false, length = 50)
	private String lastName;

	@NotBlank(message = "Email is required.")
	@Email(message = "Please provide a valid email address.")
	@Size(max = 254, message = "Email must not exceed 254 characters.")
	@Column(nullable = false, unique = true, length = 254)
	private String email;

	@NotBlank(message = "Phone number is required.")
	@Pattern(
			regexp = "^\\+?[0-9 ()-]{7,20}$",
			message = "Please provide a valid phone number."
			)

	@NotBlank(message = "Phone number is required.")
	@Pattern(
			regexp = "^\\+639\\d{9}$",
			message = "Phone number must start with +639 and contain exactly 13 characters, e.g. +639123456789."
			)
	@Column(nullable = false, length = 13)
	private String phoneNumber;


	@NotNull(message = "Number of guests is required.")
	@Min(value = 1, message = "There must be at least one guest.")
	@Column(nullable = false)
	private Integer numberOfGuests;

	@NotNull(message = "Reservation date and time are required.")
	@Future(message = "Reservation date and time must be in the future.")
	@Column(nullable = false)
	private LocalDateTime reservationDate;


	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatus status = ReservationStatus.PENDING;

}
