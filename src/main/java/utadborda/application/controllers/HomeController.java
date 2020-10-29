package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import utadborda.application.Entities.Restaurant;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/restaurant")
    public String restaurantPage(Model model) {
        Restaurant restaurant = new Restaurant("Empty", "Empty", "Empty", null);
        model.addAttribute("restaurant", restaurant);
        return "restaurant";
    }
}
