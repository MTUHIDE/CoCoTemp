package space.hideaway.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.User;
import space.hideaway.services.DashboardServiceImplementation;
import space.hideaway.services.SecurityServiceImplementation;
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

    /**
     * The service responsible for managing CRUD operations on users.
     */
    private final UserServiceImplementation userServiceImplementation;
    /**
     * The service responsible for various dashboard features.
     */
    private final DashboardServiceImplementation dashboardServiceImplementation;
    /**
     * The security service for various session operations.
     */
    private final SecurityServiceImplementation securityServiceImplementation;

    /**
     * Set the default temperature unit for display.
     */
    @Value("${cocotemp.temperature.unit}")
    String temperatureUnit;

    /**
     * Class level logger.
     */
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    public DashboardController(UserServiceImplementation userServiceImplementation, DashboardServiceImplementation dashboardServiceImplementation, SecurityServiceImplementation securityServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
        this.dashboardServiceImplementation = dashboardServiceImplementation;
        this.securityServiceImplementation = securityServiceImplementation;
    }

    /**
     * @param model The Spring maintained object model.
     * @return The name of the template to be rendered.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            User user = userServiceImplementation.findByUsername(securityServiceImplementation.findLoggedInUsername());
            model.addAttribute("devices", user.getDeviceSet());
            model.addAttribute("dashboardServiceImplementation", dashboardServiceImplementation);
            model.addAttribute("records", dashboardServiceImplementation.getNumberOfRecords(user));
            model.addAttribute("data", dashboardServiceImplementation.getAllData(user));
            model.addAttribute("temperatureUnit", temperatureUnit);
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the dashboard page.", e);
        }
        return "dashboard";
    }
}
