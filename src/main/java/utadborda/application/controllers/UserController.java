package utadborda.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utadborda.application.Entities.User;
import utadborda.application.Exceptions.GeneralExceptions;
import utadborda.application.services.DTO.UserDTO;
import utadborda.application.services.UserService;
import utadborda.application.web.requestMappings;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = requestMappings.LOGIN)
    public String getLoginView(RedirectAttributes model) {
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.LOGIN_RETRY)
    public String getLoginRetry(RedirectAttributes model) {
        model.addFlashAttribute("retry", true);
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.LOGIN_SUCCESS)
    public String getLoginSuccessView(
            @AuthenticationPrincipal UserDetails user,
            RedirectAttributes model
    ) {
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.LOGOUT_SUCCESS)
    public String getLogoutSuccessView(RedirectAttributes model) {
        return "redirect:/";
    }

    @RequestMapping(value = requestMappings.SIGNUP, method = GET)
    public String getSignupView(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    @RequestMapping(value = requestMappings.SIGNUP, method = POST)
    public String registerUser(
            @ModelAttribute @Valid UserDTO user,
            RedirectAttributes model
            ) {
        try {
            User registered = userService.registerNewUser(user);
        } catch (GeneralExceptions.UserAlreadyExistsException uaeEx) {
            model.addFlashAttribute("Error", "An account for that username/email already exists");
            return "signup";
        }
        return "redirect:/";
    }
}
