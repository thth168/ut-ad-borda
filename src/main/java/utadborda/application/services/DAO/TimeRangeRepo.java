package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.TimeRange;

import java.util.UUID;

public interface TimeRangeRepo extends JpaRepository<TimeRange, UUID> {

}
