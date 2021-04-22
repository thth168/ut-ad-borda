package utadborda.application.RestControllers;

import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;
import com.github.dhiraj072.randomwordgenerator.datamuse.DataMuseRequest;
import com.github.dhiraj072.randomwordgenerator.datamuse.WordsRequest;
import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestAddTagDTO;
import utadborda.application.services.DTO.RestTagCategoryDTO;
import utadborda.application.services.DTO.RestTagDTO;
import utadborda.application.services.DTO.RestRestaurantListDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;

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
            @RequestParam(required = false) Optional<Double> lng,
            @RequestParam(required = false) Optional<Double> distance
    ) {
        List<Restaurant> restaurants;
        long count;
        long maxNumOfPages;

        if (tag != null) {
            Tag foundTag = tagService.getTagById(tag);
            if (lat.isPresent() && lng.isPresent() && distance.isPresent()) {
                restaurants = restaurantService.getAllByTagAndGPS( foundTag, lat.get(), lng.get(), distance.get(), page, limit);
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

    @GetMapping(value = requestMappings.API_3_FOOD_WORDS)
    String get3Words() throws DataMuseException {
        String randomWord = "";
        WordsRequest customRequest = new DataMuseRequest().topics("pasta");
        randomWord += RandomWordGenerator.getRandomWord(customRequest) + "-";
        customRequest = new DataMuseRequest().topics("food", "dinner", "cooking", "restaurant");
        randomWord += RandomWordGenerator.getRandomWord(customRequest) + "-";
        customRequest = new DataMuseRequest().topics("surprise", "happy", "glad");
        randomWord += RandomWordGenerator.getRandomWord(customRequest);
        return randomWord;
    }

    @PostMapping(value = requestMappings.API_ADD_TAG, consumes = "application/json;charset=UTF-8")
    Tag addTag(@RequestBody RestAddTagDTO restTagDTO) {
        Tag tag = new Tag(restTagDTO.getName(), restTagDTO.getCategory());
        tag = tagService.addTag(tag);
        return tag;
    }

    @PostMapping(value = requestMappings.API_ADD_TAG_TO_RESTAURANT)
    Restaurant addTagToRestaurant(@RequestParam UUID restaurant_id, @RequestParam UUID tag_id) {
        Tag tag = tagService.getTagById(tag_id);
        Restaurant restaurant = restaurantService.getByID(restaurant_id);
        restaurant.addTag(tag);
        tag.addRestaurant(restaurant);
        restaurant = restaurantService.updateRestaurant(restaurant);
        tagService.updateTag(tag);
        return restaurant;
    }
}
