package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.MenuItem;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.MenuItemRepo;
import utadborda.application.services.DAO.RestaurantRepo;
import utadborda.application.services.DAO.TagRepo;
import utadborda.application.services.DAO.TimeRangeRepo;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TimeRangeService;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepo restaurantRepo;
    private TimeRangeRepo timeRangeRepo;
    private TagRepo tagRepo;
    private MenuItemRepo menuItemRepo;
    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepo restaurantRepo,
            TimeRangeRepo timeRangeRepo,
            TagRepo tagRepo,
            MenuItemRepo menuItemRepo
    ) {
        this.restaurantRepo = restaurantRepo;
        this.timeRangeRepo = timeRangeRepo;
        this.tagRepo = tagRepo;
        this.menuItemRepo = menuItemRepo;
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
        Restaurant newRestaurant = restaurantRepo.save(restaurant);
        System.out.println(restaurant.getOpeningHours().getClass());
        for (TimeRange timeRange: restaurant.getOpeningHours()) {
            if(timeRange.getId() != null && !timeRangeRepo.existsById(timeRange.getId())){
                timeRange.setRestaurant(newRestaurant);
                timeRangeRepo.save(timeRange);
            }
        }
        return newRestaurant;
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
        System.out.println(restaurant.getOpeningHours().getClass());
        Restaurant newRestaurant = restaurantRepo.save(restaurant);
        for (TimeRange timeRange: restaurant.getOpeningHours()) {
            timeRange.setRestaurant(newRestaurant);
            timeRangeRepo.save(timeRange);
        }
        return newRestaurant;
    }

    @Override
    public boolean existsById(UUID id) {
        return restaurantRepo.existsById(id);
    }
}
