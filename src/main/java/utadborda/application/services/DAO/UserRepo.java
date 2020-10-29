package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.User;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
