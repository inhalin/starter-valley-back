package startervalley.backend.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.TeamUser;
import startervalley.backend.entity.TeamUserId;

public interface TeamUserRepository extends JpaRepository<TeamUser, TeamUserId> {
}
