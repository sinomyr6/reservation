
package restaurant.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import restaurant.reservation.entity.Reservation;
import restaurant.reservation.entity.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Query("SELECT r FROM Reservation r WHERE r.email = :email")
    List<Reservation> findReservationsByEmail(
        @Param("email") String email
    );

    @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.email = :email
          AND r.status = :status
        """)
    List<Reservation> findConfirmedReservationsByEmail(
        @Param("email") String email,
        @Param("status") ReservationStatus status
    );

    @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.status = :status
          AND r.reservationDate BETWEEN :start AND :end
        """)
    List<Reservation> findByStatusAndReservationDateBetween(
        @Param("status") ReservationStatus status,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
