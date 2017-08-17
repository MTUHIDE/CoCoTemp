package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.model.User;
import space.hideaway.services.UserServiceImplementation;


/**
 * The controller responsible for displaying the dashboard page.
 */
@Controller
public class DashboardController
{

    // The service responsible for user CRUD operations.
    private final UserServiceImplementation userServiceImplementation;

    /* Obtain the default temperature unit from the application.properties file and inject
     * it into a variable for later use.
    */
    @Value("${cocotemp.temperature.unit}")
    String temperatureUnit;

    @Autowired
    public DashboardController(UserServiceImplementation userServiceImplementation)
    {
        this.userServiceImplementation = userServiceImplementation;
    }

    /**
     * The endpoint for the dashboard view.
     * Secured: Yes
     * Method: GET
     *
     * Sample URL: /dashboard
     *
     * @param model The model maintained by Spring for the dashboard page.
     * @return The path to the dashboard template.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model)
    {
        //Obtain the logged in user.
        User user = userServiceImplementation.getCurrentLoggedInUser();
        model.addAttribute("greeting", "Hello, " + user.getUsername());
        model.addAttribute("sites", user.getSiteSet());
        model.addAttribute("devices", user.getDeviceSet());

        return "dashboard";
    }
}
