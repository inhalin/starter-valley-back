package startervalley.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import startervalley.backend.entity.Category;
import startervalley.backend.entity.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "select * from store s join category c on s.category_id = c.id order by RAND() limit 3", nativeQuery = true)
    List<Store> findAllRandom();

    Page<Store> findAll(Pageable pageable);

    Page<Store> findAllByCategory(Pageable pageable, Category category);
}
