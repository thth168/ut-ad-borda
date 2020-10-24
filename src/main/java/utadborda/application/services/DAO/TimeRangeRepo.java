package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.TimeRange;

public interface TimeRangeRepo extends JpaRepository<TimeRange, Long> {

}
