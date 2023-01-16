package startervalley.backend.repository.generation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.Generation;
import startervalley.backend.entity.QGeneration;
import startervalley.backend.entity.QTeam;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenerationRepositoryImpl implements GenerationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QGeneration generation = QGeneration.generation;
    private final QTeam team = QTeam.team;

    @Override
    public List<Generation> findAllGenerations() {
        return queryFactory.selectFrom(generation)
                .leftJoin(generation.teams, team).fetchJoin()
                .orderBy(generation.id.asc())
                .fetch();
    }
}
