package startervalley.backend.repository.devpart;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Devpart;

import java.util.Optional;

public interface DevpartRepository extends JpaRepository<Devpart, Long>, DevpartRepositoryCustom {
    Optional<Devpart> findByName(String name);

    Optional<Devpart> findByNameAndGenerationId(String name, Long generationId);

    boolean existsByNameAndGenerationId(String name, Long generationId);
}
