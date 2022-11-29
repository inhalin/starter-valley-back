package startervalley.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import startervalley.backend.entity.Attendance;
import startervalley.backend.entity.AttendanceId;
import startervalley.backend.entity.User;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

    List<Attendance> findAllByUser(User user);

    List<Attendance> findAllByStatusIsNull();

    @Query(value = "SELECT * FROM attendance WHERE user_id = :user_id " +
            "AND YEAR(attended_date) = :year " +
            "AND MONTH(attended_date) = :month", nativeQuery = true)
    List<Attendance> findAllByUserForMonth(@Param("user_id") long userId,
                                           @Param("year") int year,
                                           @Param("month") int month);
}
