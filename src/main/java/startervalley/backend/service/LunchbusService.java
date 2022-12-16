package startervalley.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.dto.lunchbus.LunchbusDto;
import startervalley.backend.dto.lunchbus.LunchbusInsertRequest;
import startervalley.backend.dto.lunchbus.LunchbusSimpleDto;
import startervalley.backend.dto.lunchbus.LunchbusUserDto;
import startervalley.backend.dto.common.BasicResponse;
import startervalley.backend.entity.Lunchbus;
import startervalley.backend.entity.User;
import startervalley.backend.exception.ResourceNotFoundException;
import startervalley.backend.exception.UserNotValidException;
import startervalley.backend.repository.LunchbusCustomRepository;
import startervalley.backend.repository.LunchbusRepository;
import startervalley.backend.service.auth.AuthService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LunchbusService {

    private final LunchbusRepository lunchbusRepository;
    private final LunchbusCustomRepository lunchbusCustomRepository;
    private final UserService userService;
    private final AuthService authService;

    @Transactional
    public BasicResponse saveLunchbus(Long userId, LunchbusInsertRequest request) {
        User driver = userService.findUserOrThrow(userId);

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

    public List<LunchbusSimpleDto> findAllByActive(Boolean active) {
        User loginUser = authService.getLoginUser();

        return lunchbusCustomRepository.findAllByActiveAndGenerationId(active, loginUser.getGeneration().getId());
    }

    public LunchbusDto findLunchbus(Long busId) {
        Lunchbus bus = lunchbusRepository.findById(busId).orElseThrow(() -> new ResourceNotFoundException("Lunchbus", "id", busId.toString()));
        User driver = bus.getDriver();
        User loginUser = authService.getLoginUser();

        return LunchbusDto.builder()
                .busId(bus.getId())
                .title(bus.getTitle())
                .description(bus.getDescription())
                .limit(bus.getOccupancy())
                .count(bus.getCount())
                .isDriver(bus.getDriver().getId().equals(loginUser.getId()))
                .isPassenger(false) // TODO: 승객 부분 만들면 반영 필요
                .driver(LunchbusUserDto.builder()
                        .imageUrl(driver.getImageUrl())
                        .name(driver.getName())
                        .build())
                .passengers(new ArrayList<>()) // TODO: 승객 부분 만들면 반영 필요
                .storeName(bus.getStoreName())
                .storeUrl(bus.getStoreUrl())
                .build();
    }

    @Transactional
    public BasicResponse deleteLunchbus(Long busId) {
        lunchbusCustomRepository.deleteOneById(busId);

        return BasicResponse.of(busId, "버스가 정상적으로 삭제되었습니다.");
    }

    public void validateDriver(Long userId, Long busId) {
        Long driverId = lunchbusCustomRepository.findById(busId).getDriver().getId();

        if (!userId.equals(driverId)) {
            throw new UserNotValidException("버스 기사만 삭제할 수 있습니다.");
        }
    }
}
