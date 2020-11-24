package utadborda.application.controllers;

import jdk.nashorn.internal.objects.NativeJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.DTO.RestaurantDTO;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.RestaurantService;
import utadborda.application.web.requestMappings;
import utadborda.application.web.restaurantForm;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class RestaurantController {
    RestaurantService restaurantService;

    @Autowired
    public RestaurantController(
        RestaurantService restaurantService
    ) {
      this.restaurantService = restaurantService;
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = GET)
    public String getSignupView(Model model) {
        model.addAttribute("restaurant", new RestaurantDTO());
        return "addRestaurant";
    }

    @RequestMapping(value = requestMappings.ADD_RESTAURANT, method = POST)
    public String postSignupView(
            @ModelAttribute @Valid RestaurantDTO restaurant,
            RedirectAttributes attributes
    ) {
        System.out.println(restaurant);
        return "redirect:/";
    }
}
