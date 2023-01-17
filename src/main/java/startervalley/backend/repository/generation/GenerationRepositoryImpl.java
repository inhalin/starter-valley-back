package startervalley.backend.repository.generation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.QGeneration;
import startervalley.backend.entity.QTeam;

@Repository
@RequiredArgsConstructor
public class GenerationRepositoryImpl implements GenerationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QGeneration generation = QGeneration.generation;
    private final QTeam team = QTeam.team;
}
