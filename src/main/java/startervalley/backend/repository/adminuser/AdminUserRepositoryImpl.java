package startervalley.backend.repository.adminuser;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
