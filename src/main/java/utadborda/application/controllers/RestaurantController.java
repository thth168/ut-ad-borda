package utadborda.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestaurantController {
    @GetMapping("/restaurant{id}")
    public String restaurantData(Model model) {
        return "";
    }
}
