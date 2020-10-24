package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.OpeningHours;

public interface OpeningHoursRepo extends JpaRepository<OpeningHours, Long> {
    OpeningHours findFirstById(Long id);
}
