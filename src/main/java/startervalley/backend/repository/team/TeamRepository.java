package startervalley.backend.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
