package startervalley.backend.repository.devpart;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Devpart;

import java.util.Optional;

public interface DevpartRepository extends JpaRepository<Devpart, Long>, DevpartRepositoryCustom {
    Optional<Devpart> findByName(String name);

    Devpart findByNameAndGenerationId(String name, Long generationId);

    void deleteByNameAndGenerationId(String name, Long generationId);
}
