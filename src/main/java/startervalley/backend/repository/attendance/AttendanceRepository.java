package startervalley.backend.repository.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import startervalley.backend.admin.dto.dashboard.AttendanceStatusUserDto;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.AttendanceId;
import startervalley.backend.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId>, AttendanceRepositoryCustom {

    List<Attendance> findAllByUser(User user);

    @Query(value = "select * from attendance where user_id = :user_id order by attended_date desc", nativeQuery = true)
    List<Attendance> findAllByUserOrderByDate(@Param("user_id") Long userId);

    List<Attendance> findAllByStatusIsNull();

    @Query(value = "SELECT * FROM attendance WHERE user_id = :user_id " +
            "AND YEAR(attended_date) = :year " +
            "AND MONTH(attended_date) = :month", nativeQuery = true)
    List<Attendance> findAllByUserForMonth(@Param("user_id") long userId,
                                           @Param("year") int year,
                                           @Param("month") int month);

    @Query(value = "select * from attendance where user_id = :userId and attended_date = :attendedDate", nativeQuery = true)
    Attendance findByUserIdAndAttendedDate(@Param("userId") Long userId,
                                           @Param("attendedDate") LocalDate attendedDate);

    @Transactional
    @Modifying
    @Query(value = "update attendance set status = :status, admin_memo = :adminMemo where user_id = :userId and attended_date = :date",
            nativeQuery = true)
    void updateStatusWithMemo(@Param("userId") Long userId,
                              @Param("date") LocalDate date,
                              @Param("status") String status,
                              @Param("adminMemo") String adminMemo);

    @Query(value = "select attended_date, status, attendance.user_id, user.name from attendance left join user on attendance.user_id=user.user_id where attended_date = :date and user.generation_id = :generationId",
            nativeQuery = true)
    List<AttendanceStatusUserDto> findStatusByDateAndGenerationId(@Param("date") LocalDate date,
                                                                  @Param("generationId") Long generationId);
}
