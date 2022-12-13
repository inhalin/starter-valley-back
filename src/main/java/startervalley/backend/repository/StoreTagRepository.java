package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.StoreTag;

public interface StoreTagRepository extends JpaRepository<StoreTag, Long> {
}
