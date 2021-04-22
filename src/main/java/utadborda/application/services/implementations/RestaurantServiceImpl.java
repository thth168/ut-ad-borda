package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.*;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DAO.MenuItemRepo;
import utadborda.application.services.DAO.RestaurantRepo;
import utadborda.application.services.DAO.TagRepo;
import utadborda.application.services.DAO.TimeRangeRepo;
import utadborda.application.services.RestaurantService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepo restaurantRepo;
    private final TimeRangeRepo timeRangeRepo;
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

    @Transactional
    @Override
    public Restaurant getByName(String name) throws GeneralExceptions.RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepo.findByName(name);
        if(restaurant == null) {
            throw new GeneralExceptions.RestaurantNotFoundException();
        }
        return restaurant;
    }

    @Transactional
    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        Restaurant newRestaurant = restaurantRepo.save(restaurant);
        if (restaurant.getOpeningHours() != null) {
            for (TimeRange timeRange: restaurant.getOpeningHours()) {
                if(timeRange.getId() != null && !timeRangeRepo.existsById(timeRange.getId())){
                    //timeRange.addRestaurant(newRestaurant);
                    timeRangeRepo.save(timeRange);
                }
            }
        }
        return newRestaurant;
    }

    @Transactional
    @Override
    public List<Restaurant> addRestaurants(List<Restaurant> restaurants) {
        return restaurantRepo.saveAll(restaurants);
    }

    @Transactional
    @Override
    public Restaurant getByID(UUID restaurant_ID) {
        return restaurantRepo.findByid(restaurant_ID);
    }

    @Transactional
    @Override
    public List<Restaurant> getAll() {
        return restaurantRepo.findTop20ByIdNotNull();
    }

    @Transactional
    @Override
    public List<Restaurant> getAll(int page, int limit) {
        Pageable paging = PageRequest.of(page, limit);
        return restaurantRepo.findAllByIdNotNull(paging);
    }

    @Transactional
    @Override
    public long getRestaurantCount() {
        return restaurantRepo.count();
    }

    @Transactional
    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        Restaurant newRestaurant = restaurantRepo.save(restaurant);
        for (TimeRange timeRange: restaurant.getOpeningHours()) {
            //timeRange.addRestaurant(newRestaurant);
            timeRangeRepo.save(timeRange);
        }
        return newRestaurant;
    }

    @Transactional
    @Override
    public boolean existsById(UUID id) {
        return restaurantRepo.existsById(id);
    }

    @Transactional
    @Override
    public boolean claimRestaurant(UAB_User UABUser, Restaurant restaurant) {
        UABUser.getRestaurants().add(restaurant);
        restaurant.setOwner(UABUser);
        return true;
    }

    @Transactional
    @Override
    public List<Restaurant> getAllByTag(List<Tag> tag, int page, int limit) {
        Pageable paging = PageRequest.of(page, limit);
        return restaurantRepo.findAllByTagsIsContaining(tag, paging);
    }

    @Transactional
    @Override
    public long getCountByTag(Tag tag) {
        return restaurantRepo.countAllByTagsContaining(tag);
    }

    @Transactional
    @Override
    public List<Restaurant> getAllByTagAndGPS(List<Tag> tag, double lat, double lng, double distance, int page, int limit) {
        Pageable paging = PageRequest.of(page, limit);
        return restaurantRepo.findAllByGPS(lat, lng, distance, paging);
    }
}
