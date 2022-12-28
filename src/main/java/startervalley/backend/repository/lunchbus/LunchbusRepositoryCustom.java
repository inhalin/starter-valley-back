package startervalley.backend.repository.lunchbus;

import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;

import java.util.List;

public interface LunchbusRepositoryCustom {

    List<LunchbusSimpleDto> findAllActiveByGenerationId(Long generationId);

    List<LunchbusSimpleDto> findAllNotActiveInLimitedDaysByGenerationId(int days, Long generationId);

    boolean isCreateLimitExceeded(Long userId);

    void updateCountByBusId(int count, Long busId);

    void closeById(Long busId);

    boolean isJoinLimitExceeded(Long busId);
}
