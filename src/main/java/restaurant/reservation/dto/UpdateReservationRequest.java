
package restaurant.reservation.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationRequest {

    @NotNull(message = "Reservation date and time are required.")
    @Future(message = "Reservation date and time must be in the future.")
    private LocalDateTime reservationDate;

    @NotNull(message = "Number of guests is required.")
    @Min(value = 1, message = "There must be at least one guest.")
    private Integer numberOfGuests;

}
