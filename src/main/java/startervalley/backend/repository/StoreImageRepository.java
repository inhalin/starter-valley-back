package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.StoreImage;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
}
