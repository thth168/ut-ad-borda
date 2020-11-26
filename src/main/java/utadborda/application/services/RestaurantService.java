package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.User;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    Restaurant getByName(String name);
    List<Restaurant> getAll();
    Restaurant addRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(Restaurant restaurant);
    boolean existsById(UUID id);
    Restaurant getByID(UUID restaurant_ID);
    boolean claimRestaurant(User user, Restaurant restaurant);
    List<Restaurant> findAllByTag(Tag tag);
}
