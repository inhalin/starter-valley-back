package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
