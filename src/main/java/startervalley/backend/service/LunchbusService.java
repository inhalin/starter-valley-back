package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.dto.lunchbus.LunchbusDto;
import startervalley.backend.dto.lunchbus.LunchbusInsertRequest;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.dto.lunchbus.LunchbusUserDto;
import startervalley.backend.entity.Lunchbus;
import startervalley.backend.entity.Passenger;
import startervalley.backend.entity.User;
import startervalley.backend.exception.CustomLimitExceededException;
import startervalley.backend.exception.LunchbusInvalidJobException;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserNotValidException;
import startervalley.backend.repository.lunchbus.LunchbusRepository;
import startervalley.backend.repository.lunchbus.PassengerRepository;
import startervalley.backend.service.auth.AuthService;

import java.util.List;

import static startervalley.backend.constant.CodedErrorMessage.*;
import static startervalley.backend.constant.LimitMessage.ACTIVE_LUNCHBUS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LunchbusService {

    private final LunchbusRepository lunchbusRepository;
    private final PassengerRepository passengerRepository;
    private final UserService userService;
    private final AuthService authService;

    @Transactional
    public BasicResponse saveLunchbus(Long userId, LunchbusInsertRequest request) {
        User driver = userService.findUserOrThrow(userId);

        if (!lunchbusRepository.isAvailableToInsert(userId)) {
            throw new CustomLimitExceededException(ACTIVE_LUNCHBUS.getMessage(), ACTIVE_LUNCHBUS.getLimit());
        }

        Lunchbus bus = Lunchbus.builder()
                .driver(driver)
                .title(request.getTitle())
                .description(request.getDescription())
                .occupancy(request.getLimit())
                .count(1)
                .storeName(request.getStoreName())
                .storeUrl(request.getStoreUrl())
                .active(true)
                .build();

        lunchbusRepository.save(bus);

        return BasicResponse.of(bus.getId(), "버스가 정상적으로 생성되었습니다.");
    }

    public List<LunchbusSimpleDto> findAllActiveLunchbuses(Long userId) {
        User loginUser = userService.findUserOrThrow(userId);

        return lunchbusRepository.findAllActiveByGenerationId(loginUser.getGeneration().getId());
    }

    public List<LunchbusSimpleDto> findPastLunchbusesInLimitedDays(int days, Long generationId) {
        return lunchbusRepository.findAllNotActiveInLimitedDaysByGenerationId(days, generationId);
    }

    public LunchbusDto findLunchbus(Long busId) {
        Lunchbus bus = lunchbusRepository.findById(busId).orElseThrow(() -> new ResourceNotFoundException("Lunchbus", "id", busId.toString()));
        User driver = bus.getDriver();
        User loginUser = authService.getLoginUser();
        List<LunchbusUserDto> passengers = passengerRepository.findAllByBusId(busId);
        boolean isPassenger = passengers.stream().anyMatch(passenger -> passenger.getUserId().equals(loginUser.getId()));

        return LunchbusDto.builder()
                .busId(bus.getId())
                .title(bus.getTitle())
                .description(bus.getDescription())
                .limit(bus.getOccupancy())
                .count(bus.getCount())
                .isDriver(bus.getDriver().getId().equals(loginUser.getId()))
                .isPassenger(isPassenger)
                .driver(LunchbusUserDto.builder()
                        .userId(driver.getId())
                        .imageUrl(driver.getImageUrl())
                        .name(driver.getName())
                        .build())
                .passengers(passengers)
                .storeName(bus.getStoreName())
                .storeUrl(bus.getStoreUrl())
                .build();
    }

    @Transactional
    public BasicResponse deleteLunchbus(Long busId) {

        lunchbusRepository.deleteOneById(busId);

        return BasicResponse.of(busId, "버스가 정상적으로 삭제되었습니다.");
    }

    public void validateDriver(Long userId, Long busId) {
        Long driverId = findByIdOrElseThrow(busId).getDriver().getId();

        if (!userId.equals(driverId)) {
            throw new UserNotValidException("버스 기사만 접근 가능합니다.");
        }
    }

    @Transactional
    public BasicResponse joinLunchbus(Long userId, Long busId) {

        Lunchbus lunchbus = findByIdOrElseThrow(busId);
        User loginUser = userService.findUserOrThrow(userId);

        // 운행중인 버스만 탑승 가능
        if (!lunchbus.isActive()) {
            throw new LunchbusInvalidJobException(LUNCHBUS_INACTIVE.getMessage(), LUNCHBUS_INACTIVE.getCode());
        }

        // 인원 초과된 경우 탑승 불가
        if (lunchbusRepository.isLimitExceeded(busId)) {
            throw new LunchbusInvalidJobException(LUNCHBUS_EXCEEDED.getMessage(), LUNCHBUS_EXCEEDED.getCode());
        }

        // 본인 버스 탑승 불가
        if (lunchbus.getDriver().getId().equals(loginUser.getId())) {
            throw new LunchbusInvalidJobException(LUNCHBUS_OWNER.getMessage(), LUNCHBUS_OWNER.getCode());
        }

        // 중복 탑승 불가
        if (passengerRepository.existsOnTheBus(lunchbus.getId(), loginUser.getId())) {
            throw new LunchbusInvalidJobException(LUNCHBUS_ALREADY_JOINED.getMessage(), LUNCHBUS_ALREADY_JOINED.getCode());
        }

        Passenger passenger = Passenger.builder()
                .lunchbus(lunchbus)
                .user(loginUser)
                .build();
        passengerRepository.save(passenger);
        lunchbusRepository.updateCountByBusId(lunchbus.getCount() + 1, lunchbus.getId());

        return BasicResponse.of(busId, "버스에 탑승하였습니다.");
    }

    @Transactional
    public BasicResponse leaveLunchbus(Long userId, Long busId) {

        Lunchbus lunchbus = findByIdOrElseThrow(busId);
        User loginUser = userService.findUserOrThrow(userId);

        // 운행 종료된 경우 하차 불가
        if (!lunchbus.isActive()) {
            throw new LunchbusInvalidJobException(LUNCHBUS_INACTIVE.getMessage(), LUNCHBUS_INACTIVE.getCode());
        }

        // 본인 버스 하차 불가
        if (lunchbus.getDriver().getId().equals(loginUser.getId())) {
            throw new LunchbusInvalidJobException(LUNCHBUS_OWNER.getMessage(), LUNCHBUS_OWNER.getCode());
        }

        // 본인 미탑승 버스 하차 불가
        if (!passengerRepository.existsOnTheBus(lunchbus.getId(), loginUser.getId())) {
            throw new LunchbusInvalidJobException(LUNCHBUS_NOT_PASSENGER.getMessage(), LUNCHBUS_NOT_PASSENGER.getCode());
        }

        passengerRepository.deleteByBusIdAndUserId(lunchbus.getId(), loginUser.getId());
        lunchbusRepository.updateCountByBusId(lunchbus.getCount() - 1, lunchbus.getId());

        return BasicResponse.of(busId, "버스에서 하차하였습니다.");
    }

    public BasicResponse closeLunchbus(Long busId) {

        lunchbusRepository.closeById(busId);

        return BasicResponse.of(busId, "버스 운행이 종료되었습니다.");
    }

    private Lunchbus findByIdOrElseThrow(Long id) {
        return lunchbusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lunchbus", "id", id.toString()));
    }
}
