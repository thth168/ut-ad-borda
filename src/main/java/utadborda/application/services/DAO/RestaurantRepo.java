package utadborda.application.services.DAO;

import org.jruby.runtime.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepo extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findTop20ByIdNotNull();

    Restaurant findByName(String name);

    Restaurant findByid(UUID restaurant_ID);
}