package startervalley.backend.repository.attendance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
