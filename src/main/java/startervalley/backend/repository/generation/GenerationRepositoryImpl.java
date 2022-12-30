package startervalley.backend.repository.generation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GenerationRepositoryImpl implements GenerationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
