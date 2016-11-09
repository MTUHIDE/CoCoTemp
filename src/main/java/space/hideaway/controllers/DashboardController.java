package space.hideaway.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final
    UserServiceImplementation userServiceImplementation;
    private final DashboardServiceImplementation dashboardServiceImplementation;
    private final SecurityServiceImplementation securityServiceImplementation;

    @Value("${cocotemp.temperature.unit}")
    String temperatureUnit;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    public DashboardController(UserServiceImplementation userServiceImplementation, DashboardServiceImplementation dashboardServiceImplementation, SecurityServiceImplementation securityServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
        this.dashboardServiceImplementation = dashboardServiceImplementation;
        this.securityServiceImplementation = securityServiceImplementation;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = userServiceImplementation.findByUsername(securityServiceImplementation.findLoggedInUsername());
            model.addAttribute("devices", user.getDeviceSet());
            model.addAttribute("records", dashboardServiceImplementation.getNumberOfRecords(user));
            model.addAttribute("data", dashboardServiceImplementation.getAllData(user));
            model.addAttribute("temperatureUnit", temperatureUnit);
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the dashboard page.", e);
        }
        return "dashboard";
    }
}
