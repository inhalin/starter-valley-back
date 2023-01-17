package startervalley.backend.repository.devpart;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.Devpart;
import startervalley.backend.entity.QDevpart;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DevpartRepositoryImpl implements DevpartRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QDevpart devpart = QDevpart.devpart;

    @Override
    public List<Devpart> findAllByGenerationId(Long generationId) {
        return queryFactory.selectFrom(devpart)
                .where(devpart.generationId.eq(generationId))
                .fetch();
    }
}
