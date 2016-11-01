package space.hideaway.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

/**
 * UI model for basic static routes that contain no other logic than to display a template.
 * <p>
 * Created by dough on 9/21/2016.
 * Enhanced by caden on 10/09/2016.
 */
@Controller
public class RouteController {

    Logger logger = Logger.getLogger(getClass());

    @Autowired
    UserServiceImplementation userServiceImplementation;

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        model.addAttribute("userForm", new User());
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("userForm", new User());
        return "about";
    }

    @GetMapping("/appLogin")
    public String appLogin() {
        return "appLogin";
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            model.addAttribute("deviceList", userServiceImplementation.getDevices(authentication.getName()));
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the manage page.", e);
        }
        return "manage";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("userForm", new User());
        return "contact";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
