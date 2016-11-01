package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.services.UserServiceImplementation;

/**
 * HIDE CoCoTemp 2016
 * Controller responsible for user-dashboard view layer. For handling template requests as well
 * as JSON/REST responses.
 *
 * @author Piper Dougherty
 */
@Controller
public class DashboardController {

    private final
    UserServiceImplementation userServiceImplementation;

    @Autowired
    public DashboardController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("devices", userServiceImplementation.getDevices(authentication.getName()));
        return "dashboard";
    }
}
