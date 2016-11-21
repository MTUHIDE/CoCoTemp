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
import space.hideaway.services.DashboardServiceImplementation;
import space.hideaway.services.UserManagementImpl;


@Controller
public class RouteController {

    private final
    UserManagementImpl userManagementImpl;
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    private DashboardServiceImplementation dashboardServiceImplementation;

    @Autowired
    public RouteController(UserManagementImpl userManagementImpl) {
        this.userManagementImpl = userManagementImpl;
    }

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

    @GetMapping("/settings/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/settings/devices")
    public String devices(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            model.addAttribute("deviceList", userManagementImpl.getDevices(authentication.getName()));
            model.addAttribute("dashboardServiceImplementation", dashboardServiceImplementation);
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the devices page.", e);
        }
        return "devices";
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
