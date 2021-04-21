package utadborda.application.services.DAO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepo extends JpaRepository<Tag, UUID> {
    List<Tag> findAllByCategory(String category);
    List<Tag> findAllByCategory(String category, Pageable pageable);
    Tag findByCategoryAndName(String category, String name);

    @Query(value = "SELECT DISTINCT Tag.category FROM Tag", nativeQuery = true)
    List<String> findAllDistinctCategoryFromTag();
}
