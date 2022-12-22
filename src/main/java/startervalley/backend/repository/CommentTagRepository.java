package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.CommentTag;

public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
}
