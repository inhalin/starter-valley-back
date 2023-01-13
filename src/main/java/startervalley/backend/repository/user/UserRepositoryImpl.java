package startervalley.backend.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.QUser;
import startervalley.backend.entity.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    @Override
    public List<User> listAvailableForTeamByGenerationId(Long generationId) {
        return queryFactory.selectFrom(user)
                .where(user.generation.id.eq(generationId)
                        .and(user.active.eq(true)))
                .fetch();
    }
}
