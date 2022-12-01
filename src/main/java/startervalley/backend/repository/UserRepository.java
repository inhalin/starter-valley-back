package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
