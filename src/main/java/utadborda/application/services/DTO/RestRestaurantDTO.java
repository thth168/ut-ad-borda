package utadborda.application.services.DTO;

import utadborda.application.Entities.Restaurant;

import java.util.List;

public class RestRestaurantDTO {
    private final long count;
    private final long maxPages;
    private final List<Restaurant> restaurants;

    public RestRestaurantDTO(
            long count,
            long maxPages,
            List<Restaurant> restaurants
    ) {
        this.count = count;
        this.maxPages = maxPages;
        this.restaurants = restaurants;
    }

    public long getCount() {
        return count;
    }

    public long getMaxPages() {
        return maxPages;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
