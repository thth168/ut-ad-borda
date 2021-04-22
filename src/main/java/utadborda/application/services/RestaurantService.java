package utadborda.application.services;

import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.UAB_User;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    Restaurant getByName(String name);
    List<Restaurant> getAll();
    List<Restaurant> getAll(int page, int limit);
    long getRestaurantCount();
    Restaurant addRestaurant(Restaurant restaurant);
    List<Restaurant> addRestaurants(List<Restaurant> restaurants);
    Restaurant updateRestaurant(Restaurant restaurant);
    boolean existsById(UUID id);
    Restaurant getByID(UUID restaurant_ID);
    boolean claimRestaurant(UAB_User UABUser, Restaurant restaurant);
    List<Restaurant> getAllByTag(List<Tag> tag, int page, int limit);
    long getCountByTag(Tag tag);
    List<Restaurant> getAllByTagAndGPS(List<Tag> tag, double lat, double lng, double distance, int page, int limit);
}
