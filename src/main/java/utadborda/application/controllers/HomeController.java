package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.RestaurantService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class HomeController {
    private RestaurantService restaurantService;

    @Autowired
    public HomeController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("restaurants", restaurantService.getAll());
        model.addAttribute("categories", Arrays.asList("Quick", "Dirty", "Delicious"));
        return "home";
    }

    @GetMapping("/restaurant")
    public String restaurantPage(Model model) {
        Restaurant restaurant = new Restaurant("Empty", "Empty");
        model.addAttribute("restaurant", restaurant);
        return "restaurant";
    }
}
