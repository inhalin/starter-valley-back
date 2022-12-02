package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import startervalley.backend.entity.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "SELECT * FROM store s join category c on s.category_id = c.id order by RAND() limit 3", nativeQuery = true)
    List<Store> findAllRandom();
}
