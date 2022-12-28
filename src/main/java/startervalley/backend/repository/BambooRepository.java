package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Bamboo;

public interface BambooRepository extends JpaRepository<Bamboo, Long> {
}
