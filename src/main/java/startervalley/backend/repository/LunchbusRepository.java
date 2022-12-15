package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Lunchbus;

public interface LunchbusRepository extends JpaRepository<Lunchbus, Long> {
}
