package startervalley.backend.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import startervalley.backend.dto.lunchbus.LunchbusUserDto;
import startervalley.backend.entity.Passenger;

import java.util.ArrayList;
import java.util.List;

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

    public List<LunchbusUserDto> findAllPassengersByBusId(Long busId) {

        List<Tuple> tuple = queryFactory.select(passenger.user.id, passenger.user.name, passenger.user.imageUrl)
                .from(passenger)
                .where(passenger.lunchbus.id.eq(busId))
                .fetch();

        List<LunchbusUserDto> dtoList = new ArrayList<>();
        tuple.forEach(t -> {
            LunchbusUserDto dto = LunchbusUserDto.builder()
                    .userId(t.get(passenger.user.id))
                    .name(t.get(passenger.user.name))
                    .imageUrl(t.get(passenger.user.imageUrl))
                    .build();
            dtoList.add(dto);
        });

        return dtoList;
    }
}
