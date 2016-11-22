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
import space.hideaway.services.UserManagementImpl;
import space.hideaway.services.UserService;


@Controller
public class RouteController {

    /**
     * The service responsible for operations on users.
     */
    private final UserService userService;
    /**
     * Class level logger instance.
     */
    Logger logger = Logger.getLogger(getClass());

    @Autowired
    public RouteController(UserManagementImpl userService) {
        this.userService = userService;
    }

    /**
     * The endpoint for the application home page.
     * <p>
     * URL: / or /home
     * Secured: No
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the index template.
     */
    @GetMapping({"/", "/home"})
    public String index(Model model) {

        //Refers to index.html.
        return "index";
    }

    /**
     * The endpoint for the application about page.
     * <p>
     * URL: /about
     * Secured: No
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the about page template.
     */
    @GetMapping("/about")
    public String about(Model model) {

        //Refers to about.html.
        return "about";
    }

    /**
     * The endpoint for the app login page.
     * <p>
     * URL: /appLogin
     * Secured: No
     * Method: GET
     *
     * @return The name of the app login template.
     */
    @GetMapping("/appLogin")
    public String appLogin() {
        return "appLogin";
    }

    /**
     * The endpoint for the profile settings page.
     * <p>
     * URL: /settings/profile
     * Secured: Yes
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the profile settings template.
     */
    @GetMapping("/settings/profile")
    public String profile(Model model) {
        return "profile";
    }

    /**
     * The endpoint for the device settings page.
     * <p>
     * URL: /settings/devices
     * Secured: Yes
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the device settings template.
     */
    @GetMapping("/settings/devices")
    public String devices(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            model.addAttribute("deviceList", userService.getDevices(authentication.getName()));
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the devices page.", e);
        }
        return "devices";
    }

    /**
     * The endpoint for the contact page.
     * <p>
     * URL: /contact
     * Secured: No
     * Method: GET
     *
     * @param model The model maintained by Spring.
     * @return The name of the contact page template.
     */
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("userForm", new User());
        return "contact";
    }

    /**
     * The endpoint for the error page.
     * <p>
     * URL: /error
     * Secured: No
     * Method: GET
     *
     * @return The name of the error page template.
     */
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
