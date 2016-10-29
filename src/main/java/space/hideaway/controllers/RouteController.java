package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;

/**
 * HIDE CoCoTemp 2016
 * Class responsible for routing generic requests to pages that are not very logic heavy.
 *
 * @author Piper Dougherty
 * @author Caden Sumner
 */
@Controller
public class RouteController {

    /**
     * The service responsible for obtaining and performing operations on user-accounts.
     */
    @Autowired
    UserServiceImplementation userServiceImplementation;

    /**
     * The route responsible for displaying the index page.
     *
     * @param model The object model maintained by Spring.
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping({"/", "/home"})
    public String index(Model model) {

        //Insert a generic user for the included registration form.
        model.addAttribute("userForm", new User());

        return "index";
    }

    /**
     * The route responsible for displaying the about page.
     *
     * @param model The object model maintained by Spring.
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/about")
    public String about(Model model) {

        //Insert a generic user for the included registration form.
        model.addAttribute("userForm", new User());

        return "about";
    }

    /**
     * The route responsible for displaying the mobile login page.
     *
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/appLogin")
    public String appLogin() {
        return "appLogin";
    }

    /**
     * The route responsible for displaying the user dashboard.
     *
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("devices", userServiceImplementation.getDevices(authentication.getName()));
        return "dashboard";
    }

    /**
     * The route responsible for displaying the settings page.
     *
     * @param model The object model maintained by Spring.
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/manage")
    public String manage(Model model) {

        //Get the current context for the logged in user.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Add a list of devices to the settings page for the current logged in user.
        model.addAttribute("deviceList", userServiceImplementation.getDevices(authentication.getName()));

        return "manage";
    }

    /**
     * The route responsible for displaying the contact page.
     *
     * @param model The object model maintained by Spring.
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/contact")
    public String contact(Model model) {

        //Insert a generic user for the included registration form.
        model.addAttribute("userForm", new User());
        return "contact";
    }

    /**
     * The route responsible for displaying the error page.
     *
     * @return The name of the template to be rendered by the template engine.
     */
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
