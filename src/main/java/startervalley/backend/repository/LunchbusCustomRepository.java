package startervalley.backend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.entity.Lunchbus;

import java.util.List;

import static startervalley.backend.entity.QLunchbus.lunchbus;

@Repository
@RequiredArgsConstructor
public class LunchbusCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Lunchbus findById(Long busId) {

        return queryFactory
                .selectFrom(lunchbus)
                .where(lunchbus.id.eq(busId))
                .fetchFirst();
    }

    public List<LunchbusSimpleDto> findAllByActiveAndGenerationId(Boolean active, Long generationId) {

        return queryFactory
                .select(Projections.bean(
                        LunchbusSimpleDto.class,
                        lunchbus.id.as("busId"),
                        lunchbus.title,
                        lunchbus.occupancy.as("limit"),
                        lunchbus.driver.name.as("driverName"),
                        lunchbus.count,
                        lunchbus.driver.imageUrl.as("driverImageUrl")))
                .from(lunchbus)
                .where(lunchbus.active.eq(active), lunchbus.driver.generation.id.eq(generationId))
                .fetch();
    }

    public void deleteOneById(Long busId) {

        queryFactory
                .delete(lunchbus)
                .where(lunchbus.id.eq(busId))
                .execute();
    }
}
