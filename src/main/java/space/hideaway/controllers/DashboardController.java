package space.hideaway.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.DashboardServiceImplementation;
import space.hideaway.services.DataServiceImplementation;
import space.hideaway.services.SecurityServiceImplementation;
import space.hideaway.services.UserManagementImpl;

import java.util.Comparator;
import java.util.Set;

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
    private final UserManagementImpl userManagementImpl;
    /**
     * The service responsible for various dashboard features.
     */
    private final DashboardServiceImplementation dashboardServiceImplementation;
    /**
     * The security service for various session operations.
     */
    private final SecurityServiceImplementation securityServiceImplementation;

    private final DataServiceImplementation dataServiceImplementation;

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
    public DashboardController(UserManagementImpl userManagementImpl, DashboardServiceImplementation dashboardServiceImplementation, SecurityServiceImplementation securityServiceImplementation, DataServiceImplementation dataServiceImplementation) {
        this.userManagementImpl = userManagementImpl;
        this.dashboardServiceImplementation = dashboardServiceImplementation;
        this.securityServiceImplementation = securityServiceImplementation;
        this.dataServiceImplementation = dataServiceImplementation;
    }

    /**
     * @param model The Spring maintained object model.
     * @return The name of the template to be rendered.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            User user = userManagementImpl.findByUsername(securityServiceImplementation.findLoggedInUsername());
            Set<Device> deviceSet = user.getDeviceSet();

            model.addAttribute("devicesComparator", (Comparator<Device>) (o1, o2) -> o1.getDeviceName().compareTo(o2.getDeviceName()));
            model.addAttribute("devices", deviceSet);
            model.addAttribute("dashboardServiceImplementation", dashboardServiceImplementation);
            model.addAttribute("temperatureUnit", temperatureUnit);
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when loading the dashboard page.", e);
        }
        return "dashboard";
    }
}
