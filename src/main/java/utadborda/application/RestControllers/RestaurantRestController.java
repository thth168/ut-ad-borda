package utadborda.application.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestTagDTO;
import utadborda.application.services.DTO.RestRestaurantListDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) UUID tag
    ) {
        List<Restaurant> restaurants;
        long count;
        long maxNumOfPages;

        if (tag != null) {
            Tag foundTag = tagService.getTagById(tag);
            restaurants = restaurantService.getAllByTag(foundTag, page, limit);
            count = restaurantService.getCountByTag( foundTag );
            maxNumOfPages = count / limit;
        } else {
            restaurants = restaurantService.getAll(page, limit);
            count = restaurantService.getRestaurantCount();
            maxNumOfPages = count / limit;
        }

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

    @GetMapping(value = requestMappings.API_RESTAURANT)
    Restaurant getRestaurant(
            @RequestParam UUID id
    ) {
        return restaurantService.getByID(id);
    }
}
