package restaurant.reservation;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
		
//		LocalDateTime testDate = LocalDateTime.now().plusMinutes(2);
//		System.out.println("Date Now + 2 minutes: " + testDate);
		
		LocalDateTime testDateNowPlus4Hours = LocalDateTime.now().plusHours(4).plusMinutes(5);
		System.out.println("Date Now + 4 Hours and 5 Minutes: "+testDateNowPlus4Hours);
	}

}
