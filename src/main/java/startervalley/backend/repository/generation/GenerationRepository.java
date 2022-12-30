package startervalley.backend.repository.generation;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Generation;

public interface GenerationRepository extends JpaRepository<Generation, Long>, GenerationRepositoryCustom {
}
