package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;
    @Value("")
    String restaurant;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/restaurant")
    public String restaurantPage(Model model) {
        model.addAttribute("restaurant", restaurant);
        return "restaurant";
    }
}
