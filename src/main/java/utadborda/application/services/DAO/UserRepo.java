package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.UAB_User;

import java.util.UUID;

public interface    UserRepo extends JpaRepository<UAB_User, UUID> {
    boolean existsByEmail(String email);

    UAB_User findByEmail(String email);
}
