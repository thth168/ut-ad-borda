package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.TimeRange;

import java.util.List;

public interface RestaurantService {
    Restaurant getByName(String name);
    List<Restaurant> getAll();
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(Restaurant restaurant);
}
