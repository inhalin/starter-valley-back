package startervalley.backend.repository.lunchbus;

import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;

import java.util.List;

public interface LunchbusRepositoryCustom {

    void deleteOneById(Long busId);

    List<LunchbusSimpleDto> findAllActiveByGenerationId(Long generationId);

    List<LunchbusSimpleDto> findAllNotActiveInLimitedDaysByGenerationId(int days, Long generationId);

    boolean isAvailableToInsert(Long userId);

    void updateCountByBusId(int count, Long busId);

    void closeById(Long busId);

    boolean isLimitExceeded(Long busId);
}
