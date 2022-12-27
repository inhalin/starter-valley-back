package startervalley.backend.repository.lunchbus;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.entity.Lunchbus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static startervalley.backend.constant.LimitMessage.ACTIVE_LUNCHBUS;
import static startervalley.backend.entity.QLunchbus.lunchbus;

@Repository
@RequiredArgsConstructor
public class LunchbusRepositoryImpl implements LunchbusRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
                .where(lunchbus.active.eq(true),
                        lunchbus.driver.generation.id.eq(generationId))
                .orderBy(lunchbus.createdDate.desc())
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
                .orderBy(lunchbus.createdDate.desc())
                .fetch();
    }

    public boolean isCreateLimitExceeded(Long userId) {

        Long count = queryFactory
                .select(lunchbus.count())
                .from(lunchbus)
                .where(lunchbus.driver.id.eq(userId), lunchbus.active.eq(true))
                .fetchOne();

        if (count == null) return false;

        return count >= ACTIVE_LUNCHBUS.getLimit();
    }

    public void updateCountByBusId(int count, Long busId) {

        queryFactory.update(lunchbus)
                .set(lunchbus.count, count)
                .where(lunchbus.id.eq(busId))
                .execute();
    }

    public void closeById(Long busId) {

        queryFactory.update(lunchbus)
                .set(lunchbus.closedDate, LocalDateTime.now())
                .set(lunchbus.active, false)
                .where(lunchbus.id.eq(busId))
                .execute();
    }

    public boolean isJoinLimitExceeded(Long busId) {

        Lunchbus bus = queryFactory.select(lunchbus)
                .from(lunchbus)
                .where(lunchbus.id.eq(busId))
                .fetchFirst();

        return bus.getCount() >= bus.getOccupancy();
    }
}