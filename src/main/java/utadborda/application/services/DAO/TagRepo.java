package utadborda.application.services.DAO;

import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepo extends JpaRepository<Tag, UUID> {

    List<Tag> findAllByCategory(String category);

    @Query(value = "SELECT DISTINCT Tag.category FROM Tag", nativeQuery = true)
    List<String> findAllDistinctCategoryFromTag();
}
