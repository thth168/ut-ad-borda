package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.DTO.RestaurantDTO;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.web.requestMappings;
import utadborda.application.web.restaurantForm;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class RestaurantController {
    RestaurantService restaurantService;

    @Autowired
    public RestaurantController(
        RestaurantService restaurantService
    ) {
      this.restaurantService = restaurantService;
    }

    @PostMapping("/restaurantData")
    public String addRestaurant (
        @ModelAttribute Restaurant restaurant,
        Model model
    ) {
        restaurantService.addRestaurant(restaurant);
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = GET)
    public String getSignupView(Model model) {
        model.addAttribute("restaurant", new RestaurantDTO());
        return "addRestaurant";
    }


    @GetMapping(value = "/restaurantData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Restaurant restaurantData(
        @RequestParam String name
    ) {
        Restaurant result = restaurantService.getByName(name);
        return result;
    }
}
