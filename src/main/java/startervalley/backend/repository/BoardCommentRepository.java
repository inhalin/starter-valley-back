package startervalley.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Board;
import startervalley.backend.entity.BoardComment;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    Page<BoardComment> findAllByBoard(Board board, Pageable pageable);
}
