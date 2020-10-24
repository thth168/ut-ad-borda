package utadborda.application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FilterController {
    @RequestMapping("/filter")
    public String filtering(Model model) {
        return "";
    }
}
