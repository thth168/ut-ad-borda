package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Entities.Tag;
import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    Restaurant getByName(String name);
    List<Restaurant> getAll();
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(Restaurant restaurant);

    Restaurant getByID(UUID restaurant_ID);

}
