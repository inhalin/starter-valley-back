package startervalley.backend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.entity.Lunchbus;

import java.time.LocalDate;
import java.util.List;

import static startervalley.backend.constant.LimitMessage.ACTIVE_LUNCHBUS;
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

    public void deleteOneById(Long busId) {

        queryFactory
                .delete(lunchbus)
                .where(lunchbus.id.eq(busId))
                .execute();
    }

    public List<LunchbusSimpleDto> findAllActiveByGenerationId(Long generationId) {

        return queryFactory
                .select(Projections.bean(
                        LunchbusSimpleDto.class,
                        lunchbus.id.as("busId"),
                        lunchbus.title,
                        lunchbus.occupancy.as("limit"),
                        lunchbus.count,
                        lunchbus.driver.name.as("driverName"),
                        lunchbus.driver.imageUrl.as("driverImageUrl")
                ))
                .from(lunchbus)
                .where(lunchbus.active.eq(true), lunchbus.driver.generation.id.eq(generationId))
                .fetch();
    }

    public List<LunchbusSimpleDto> findAllNotActiveInLimitedDaysByGenerationId(int days, Long generationId) {

        return queryFactory
                .select(Projections.bean(
                        LunchbusSimpleDto.class,
                        lunchbus.id.as("busId"),
                        lunchbus.title,
                        lunchbus.occupancy.as("limit"),
                        lunchbus.count,
                        lunchbus.driver.name.as("driverName"),
                        lunchbus.driver.imageUrl.as("driverImageUrl")
                ))
                .from(lunchbus)
                .where(lunchbus.active.eq(false),
                        lunchbus.createdDate.after(LocalDate.now().atStartOfDay().minusDays(days)),
                        lunchbus.driver.generation.id.eq(generationId))
                .fetch();
    }

    public boolean isAvailableToInsert(Long userId) {
        Long count = queryFactory
                .select(lunchbus.count())
                .from(lunchbus)
                .where(lunchbus.driver.id.eq(userId), lunchbus.active.eq(true))
                .fetchOne();

        if (count == null) return true;

        return count <= ACTIVE_LUNCHBUS.getLimit();
    }
}
