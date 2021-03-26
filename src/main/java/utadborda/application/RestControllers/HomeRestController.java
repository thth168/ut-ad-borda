package utadborda.application.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.RestaurantService;
import utadborda.application.web.requestMappings;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HomeRestController {
    RestaurantService restaurantService;

    @Autowired
    HomeRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(requestMappings.API)
    List<Restaurant> getApiListing() {
        return restaurantService.getAll();
    }
}
