package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utadborda.application.Entities.Matching_Session;

import java.util.UUID;

public interface MatchingSessionRepo extends JpaRepository<Matching_Session, UUID> {
    Matching_Session getFirstByIdNotNull();
}
