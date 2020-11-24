package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.DTO.RestaurantDTO;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;
import utadborda.application.web.requestMappings;
import utadborda.application.web.restaurantForm;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
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

    @RequestMapping(value = requestMappings.ADD_RESTAURANT)
    public String getAddRestaurantView(Model model) {
        model.addAttribute("restaurant", new RestaurantDTO());
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.RESTAURANT)
    public String getRestaurantView(Model model, @PathVariable UUID restaurant_id){
        Restaurant restaurant = restaurantService.getByID(restaurant_id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("categories", restaurant.getTags());
        return "restaurant";
    }

    @GetMapping(value = "/restaurantData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String restaurantData(
        @RequestParam String name
    ) {
        return "redirect:/";
    }
}
