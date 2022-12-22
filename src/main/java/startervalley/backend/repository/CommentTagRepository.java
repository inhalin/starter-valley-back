package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startervalley.backend.entity.Comment;
import startervalley.backend.entity.CommentTag;

import java.util.List;

public interface CommentTagRepository extends JpaRepository<CommentTag, Long> {
}
