package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.User;

import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
