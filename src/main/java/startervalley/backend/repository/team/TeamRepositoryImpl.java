package startervalley.backend.repository.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.QTeam;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QTeam team = QTeam.team;

    public boolean existsByGenerationIdAndName(Long generationId, String name) {
        return queryFactory.selectFrom(team)
                .where(team.generation.id.eq(generationId)
                        .and(team.name.eq(name)))
                .fetch() != null;
    }
}
