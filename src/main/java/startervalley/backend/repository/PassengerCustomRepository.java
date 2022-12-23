package startervalley.backend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.entity.Passenger;

import static startervalley.backend.entity.QPassenger.passenger;

@Repository
@RequiredArgsConstructor
public class PassengerCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Boolean existsOnTheBus(Long busId, Long userId) {
        Passenger p = queryFactory
                .selectFrom(passenger)
                .where(passenger.lunchbus.id.eq(busId), passenger.user.id.eq(userId))
                .fetchFirst();

        return p != null;
    }

    public void deleteByBusIdAndUserId(Long busId, Long userId) {
        queryFactory.delete(passenger)
                .where(passenger.lunchbus.id.eq(busId), passenger.user.id.eq(userId))
                .execute();
    }
}
