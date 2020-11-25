package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class HomeController {
    private RestaurantService restaurantService;
    private TagService tagService;

    @Autowired
    public HomeController(RestaurantService restaurantService, TagService tagService) {
        this.restaurantService = restaurantService;
        this.tagService = tagService;
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("restaurants", restaurantService.getAll());
        model.addAttribute("categories", tagService.getAllByCategory("type"));
        return "home";
    }
}
