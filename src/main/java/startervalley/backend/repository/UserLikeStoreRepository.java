package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Store;
import startervalley.backend.entity.User;
import startervalley.backend.entity.UserLikeStore;

import java.util.List;
import java.util.Optional;

public interface UserLikeStoreRepository extends JpaRepository<UserLikeStore, Long> {

    boolean existsByUserAndStore(User user, Store store);

    Optional<UserLikeStore> findByUserAndStore(User user, Store store);

    List<UserLikeStore> findAllByUser(User user);

    long countByStore(Store store);
}
