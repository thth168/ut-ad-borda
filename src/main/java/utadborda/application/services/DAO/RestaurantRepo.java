package utadborda.application.services.DAO;

import org.jruby.runtime.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import utadborda.application.Entities.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepo extends JpaRepository<Restaurant, UUID> {
    Restaurant findByName(String name);
}
