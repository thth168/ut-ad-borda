package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utadborda.application.Entities.Match;

import java.util.UUID;

public interface MatchRepo extends JpaRepository<Match, UUID> {
    Match getFirstByIdNotNull();
}
