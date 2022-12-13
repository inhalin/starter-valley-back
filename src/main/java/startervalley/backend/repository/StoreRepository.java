package startervalley.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startervalley.backend.entity.Category;
import startervalley.backend.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = "select distinct s from Store s " +
            "join fetch StoreImage i on s = i.store " +
            "join fetch StoreTag st on s = st.store " +
            "join fetch Tag t on st.tag = t " +
            "where s.id = :id")
    Optional<Store> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM store s join category c on s.category_id = c.id order by RAND() limit 3", nativeQuery = true)
    List<Store> findAllRandom();

    Page<Store> findAll(Pageable pageable);

    Page<Store> findAllByCategory(Pageable pageable, Category category);
}
