package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.Entities.TimeRange;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestaurantDTO;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;
import utadborda.application.web.restaurantForm;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@SessionAttributes("restaurant")
public class RestaurantController {
    RestaurantService restaurantService;
    TagService tagService;

    @Autowired
    public RestaurantController(
        RestaurantService restaurantService,
        TagService tagService
    ) {
      this.restaurantService = restaurantService;
      this.tagService = tagService;
    }

    @PostMapping("/restaurantData")
    public String addRestaurant (
        @ModelAttribute Restaurant restaurant,
        Model model
    ) {
        restaurantService.addRestaurant(restaurant);
        return "redirect:/";
    }

    @ModelAttribute("restaurant")
    public RestaurantDTO restaurantDTO() {
        return new RestaurantDTO();
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = GET)
    public String getSignupView(@ModelAttribute("restaurant") RestaurantDTO restaurant, Model model) {
        restaurant.setOpeningHours(
                restaurant.getOpeningHours()
                        .stream().sorted((Comparator.comparing(TimeRange::getWeekDay).thenComparing(TimeRange::getSpecialDate)))
                        .collect(Collectors.toList())
        );
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST, params={"addDateRow"})
    public String addDateRow(@ModelAttribute("restaurant") RestaurantDTO restaurant, RedirectAttributes attributes) {
        List<Integer> days = restaurant.getOpeningHours().stream().map(TimeRange::getWeekDay).sorted().collect(Collectors.toList());
        for(int i = 0; i < 7; i++) {
            if (!days.contains(i)){
                restaurant.addTimeRange(new TimeRange(
                        Time.valueOf("00:00:00"),
                        Time.valueOf("00:00:00"),
                        i,
                        false,
                        Date.valueOf("1970-1-1")
                ));
                return "redirect:/addRestaurant";
            }
        }
        restaurant.addTimeRange(new TimeRange(
                Time.valueOf("00:00:00"),
                Time.valueOf("00:00:00"),
                7,
                true,
                Date.valueOf(LocalDate.now())
        ));
        return "redirect:/addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST, params={"removeDateRow"})
    public String removeDateRow(@RequestParam("removeDateRow") int index, @ModelAttribute("restaurant") RestaurantDTO restaurant, RedirectAttributes attributes) {
        restaurant.removeTimeRange(index);
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST, params={"addTagRow"})
    public String addTagRow(@ModelAttribute("restaurant") RestaurantDTO restaurant, RedirectAttributes attributes) {
        restaurant.addTag(new Tag(
            "",
                ""
        ));
        return "redirect:/addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST, params={"removeTagRow"})
    public String removeTagRow(@RequestParam("removeTagRow") int index, @ModelAttribute("restaurant") RestaurantDTO restaurant, RedirectAttributes attributes) {
        restaurant.removeTag(index);
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST)
    public String postSignupView(
            @ModelAttribute @Valid RestaurantDTO restaurant,
            RedirectAttributes attributes
    ) {
        restaurantService.addRestaurant(restaurant.convertToRestaurant());
        return "redirect:/";
    }
}   
