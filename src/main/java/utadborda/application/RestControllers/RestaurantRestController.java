package utadborda.application.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestTagDTO;
import utadborda.application.services.DTO.RestRestaurantListDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantRestController {
    RestaurantService restaurantService;
    TagService tagService;

    @Autowired
    RestaurantRestController(
            RestaurantService restaurantService,
            TagService tagService
    ) {
        this.restaurantService = restaurantService;
        this.tagService = tagService;
    }

    @GetMapping(requestMappings.API_RESTAURANT_LIST)
    RestRestaurantListDTO getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Restaurant> restaurants = restaurantService.getAll(page, limit);
        long count = restaurantService.getRestaurantCount();
        long maxNumOfPages = count / limit;
        return new RestRestaurantListDTO(count, maxNumOfPages, restaurants);
    }

    @GetMapping(requestMappings.API_TAGS_LIST)
    RestTagDTO getAllTags(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<String> categories = tagService.getAllDistinctCategoryFromTag();
        List<List<Tag>> subcategories = new ArrayList<List<Tag>>();
        for (String category: categories) {
            subcategories.add(tagService.getAllByCategory(category));
        }
        return new RestTagDTO(categories, subcategories);
    }

    @GetMapping(requestMappings.API_RESTAURANT)
    String getRestaurant(
            @RequestParam String id
    ) {
        return "TEST";
    }
}
