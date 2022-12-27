package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
