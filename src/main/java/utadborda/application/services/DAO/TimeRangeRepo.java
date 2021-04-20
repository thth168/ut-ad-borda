package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.TimeRange;

import java.util.List;
import java.util.UUID;

public interface TimeRangeRepo extends JpaRepository<TimeRange, UUID> {
    TimeRange getTimeRangeByOpenTimeAndCloseTimeAndWeekDayAndHoliday(String openTime, String closeTime, int weekDay, boolean holiday);
}
