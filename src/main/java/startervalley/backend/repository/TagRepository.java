package startervalley.backend.repository;

import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Log> {

    Optional<Tag> findByContent(String content);
}
