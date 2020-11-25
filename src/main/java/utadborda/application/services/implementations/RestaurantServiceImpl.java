package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.RestaurantRepo;
import utadborda.application.services.RestaurantService;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    public RestaurantRepo restaurantRepo;
    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepo restaurantRepo
    ) {
        this.restaurantRepo = restaurantRepo;
    }

    @Override
    public Restaurant getByName(String name) throws GeneralExceptions.RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepo.findByName(name);
        if(restaurant == null) {
            throw new GeneralExceptions.RestaurantNotFoundException();
        }
        return restaurant;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepo.save(restaurant);
    }

    @Override
    public Restaurant getByID(UUID restaurant_ID) {
        return restaurantRepo.findByid(restaurant_ID);
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepo.findTop20ByIdNotNull();
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepo.save(restaurant);
    }

    @Override
    public boolean existsById(UUID id) {
        return restaurantRepo.existsById(id);
    }
}
