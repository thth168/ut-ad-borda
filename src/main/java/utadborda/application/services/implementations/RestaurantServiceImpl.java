package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.RestaurantRepo;
import utadborda.application.services.RestaurantService;

import java.util.List;

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
    public Restaurant addRestaurant(String name, String phone, String address, List<TimeRange> openingHours) {
        return restaurantRepo.save(new Restaurant(name, phone, address, openingHours));
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepo.save(restaurant);
    }
}
