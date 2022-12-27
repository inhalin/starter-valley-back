package startervalley.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Comment;
import startervalley.backend.entity.Store;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByStore(Store store, Pageable pageable);
}
