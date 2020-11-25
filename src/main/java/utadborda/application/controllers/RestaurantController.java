package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.*;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestaurantDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.services.UserService;
import utadborda.application.web.requestMappings;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@SessionAttributes("restaurant")
public class RestaurantController {
    RestaurantService restaurantService;
    TagService tagService;
    UserService userService;

    @Autowired
    public RestaurantController(
        RestaurantService restaurantService,
        TagService tagService,
        UserService userService
    ) {
      this.restaurantService = restaurantService;
      this.tagService = tagService;
      this.userService = userService;
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
    public String getSignupView(SessionStatus status, Model model) {
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
                return "addRestaurant";
            }
        }
        restaurant.addTimeRange(new TimeRange(
                Time.valueOf("00:00:00"),
                Time.valueOf("00:00:00"),
                7,
                true,
                Date.valueOf(LocalDate.now())
        ));
        restaurant.setOpeningHours(
                restaurant.getOpeningHours()
                        .stream().sorted((Comparator.comparing(TimeRange::getWeekDay).thenComparing(TimeRange::getSpecialDate)))
                        .collect(Collectors.toList())
        );
        return "addRestaurant";
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
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST, params={"removeTagRow"})
    public String removeTagRow(@RequestParam("removeTagRow") int index, @ModelAttribute("restaurant") RestaurantDTO restaurant, RedirectAttributes attributes) {
        restaurant.removeTag(index);
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST)
    public String postSignupView(
            @ModelAttribute @Valid RestaurantDTO restaurant,
            RedirectAttributes attributes,
            Principal principal,
            SessionStatus status
    ) {
        User owner = userService.findUser(principal.getName());
        if(restaurant.getId() != null && restaurantService.existsById(restaurant.getId())){

        } else {
            restaurantService.addRestaurant(restaurant.convertToRestaurant(owner));
        }
        status.setComplete();
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.RESTAURANT)
    public String getRestaurantView(Model model, @PathVariable UUID restaurant_id, SessionStatus status){
        Restaurant restaurant = restaurantService.getByID(restaurant_id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("categories", restaurant.getTags());
        status.setComplete();
        return "restaurant";
    }

    @RequestMapping(value = requestMappings.EDIT_RESTAURANT)
    public String editRestaurant(Model model, @PathVariable UUID restaurant_id) {
        model.addAttribute("restaurant", new RestaurantDTO(restaurantService.getByID(restaurant_id)));
        return "addRestaurant";
    }
}   
