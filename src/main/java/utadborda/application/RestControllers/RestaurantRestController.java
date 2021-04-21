package utadborda.application.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestTagCategoryDTO;
import utadborda.application.services.DTO.RestTagDTO;
import utadborda.application.services.DTO.RestRestaurantListDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            @RequestParam(required = false) UUID tag,
            @RequestParam(required = false) Optional<Double> lat,
            @RequestParam(required = false) Optional<Double> lng
    ) {
        List<Restaurant> restaurants;
        long count;
        long maxNumOfPages;

        if (tag != null) {
            Tag foundTag = tagService.getTagById(tag);
            if (lat.isPresent() && lng.isPresent()) {
                restaurants = restaurantService.getAllByTagAndGPS( foundTag, lat.get(), lng.get(), page, limit);
            } else {
                restaurants = restaurantService.getAllByTag( foundTag, page, limit);
            }
            count = restaurantService.getCountByTag( foundTag );
        } else {
            restaurants = restaurantService.getAll(page, limit);
            count = restaurantService.getRestaurantCount();
        }
        maxNumOfPages = count / limit;
        return new RestRestaurantListDTO(count, maxNumOfPages, restaurants);
    }

    @GetMapping(requestMappings.API_TAGS_LIST)
    RestTagDTO getAllTags(
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (category.equals("all"))
            return new RestTagDTO(tagService.getAll(page, limit));
        return new RestTagDTO(tagService.getAllByCategory(category, page, limit));
    }

    @GetMapping(requestMappings.API_TAG_CATEGORIES)
    RestTagCategoryDTO getAllTagCategory() {
        return new RestTagCategoryDTO(tagService.getAllDistinctCategoryFromTag());
    }

    @GetMapping(value = requestMappings.API_RESTAURANT)
    Restaurant getRestaurant(
            @RequestParam UUID id
    ) {
        return restaurantService.getByID(id);
    }
}
