package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.MenuItem;

import java.util.UUID;

public interface MenuItemRepo extends JpaRepository<MenuItem, UUID> {

}
