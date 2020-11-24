package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.RestaurantService;
import utadborda.application.web.restaurantForm;

import java.io.IOException;

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

    @GetMapping(value = "/restaurantData", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Restaurant restaurantData(
        @RequestParam String name
    ) {
        Restaurant result = restaurantService.getByName(name);
        return result;
    }
}
