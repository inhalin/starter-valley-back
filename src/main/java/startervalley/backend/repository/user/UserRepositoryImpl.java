package startervalley.backend.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.QTeam;
import startervalley.backend.entity.QUser;
import startervalley.backend.entity.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final QTeam team = QTeam.team;

    @Override
    public List<User> listAvailableForTeamByGenerationId(Long generationId) {
        return queryFactory.selectFrom(user)
                .where(user.generation.id.eq(generationId)
                        .and(user.active.eq(true))
                        .and(user.team.isNull()))
                .fetch();
    }

    @Override
    public List<User> findAllByTeamId(Long teamId) {
        return queryFactory.selectFrom(user)
                .leftJoin(user.team, team).fetchJoin()
                .where(user.team.id.eq(teamId))
                .fetch();
    }
}
