package utadborda.application.services.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepo extends JpaRepository<Tag, UUID> {

    List<Tag> findAllByCategory(String category);
}
