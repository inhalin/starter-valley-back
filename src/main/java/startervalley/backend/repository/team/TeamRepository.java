package startervalley.backend.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    Optional<Team> findByNameAndGeneration(String name, Generation generation);
}
