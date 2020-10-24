package utadborda.application.services;

import utadborda.application.Entities.OpeningHours;
import utadborda.application.Entities.Restaurant;

public interface RestaurantService {
    Restaurant getByName(String name);
    Restaurant addRestaurant(String name, String phone, String address, OpeningHours openingHours);
    Restaurant addRestaurant(Restaurant restaurant);
}
