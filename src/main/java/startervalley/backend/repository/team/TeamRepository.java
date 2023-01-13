package startervalley.backend.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Team;
import startervalley.backend.entity.TeamUserId;

public interface TeamRepository extends JpaRepository<Team, TeamUserId> {
}
