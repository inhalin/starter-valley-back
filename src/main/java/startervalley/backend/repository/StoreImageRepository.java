package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.StoreImage;

import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {

    List<StoreImage> findAllByIdIn(List<Long> ids);

}
