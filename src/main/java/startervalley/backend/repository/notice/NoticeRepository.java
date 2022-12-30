package startervalley.backend.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import startervalley.backend.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {
}
