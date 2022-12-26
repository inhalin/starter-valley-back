package startervalley.backend.repository.lunchbus;

import startervalley.backend.dto.lunchbus.LunchbusUserDto;

import java.util.List;

public interface PassengerRepositoryCustom {

    boolean existsOnTheBus(Long busId, Long userId);

    void deleteByBusIdAndUserId(Long busId, Long userId);

    List<LunchbusUserDto> findAllByBusId(Long busId);
}
