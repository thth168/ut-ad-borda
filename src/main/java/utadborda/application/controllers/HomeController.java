package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.Restaurant;
import utadborda.application.Entities.Tag;
import utadborda.application.services.RestaurantService;
import utadborda.application.services.TagService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public String homePage(Model model, @RequestParam(required = false) String selected) {
        model.addAttribute("appName", appName);
        model.addAttribute("categories", tagService.getAllByCategory("type"));
        if(selected != null) {
            model.addAttribute("selected", selected);
            model.addAttribute("restaurants", tagService.getByName("type", selected));
        } else {
            model.addAttribute("restaurants", restaurantService.getAll());
        }
        model.addAttribute("allTags", tagService.getAll());

        List<String> categories = tagService.getAllDistinctCategoryFromTag();
        model.addAttribute("allCategories", categories);

        return "home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String index(@RequestParam(name = "categories") String categories, RedirectAttributes attributes){
        attributes.addAttribute("allCategories", categories);
        return "redirect:/";
    }

}
