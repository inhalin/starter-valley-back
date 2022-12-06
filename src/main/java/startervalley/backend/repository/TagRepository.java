package startervalley.backend.repository;

import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Log> {
}
