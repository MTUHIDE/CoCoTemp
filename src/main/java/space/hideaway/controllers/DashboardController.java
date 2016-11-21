package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.DashboardServiceImplementation;
import space.hideaway.services.DataServiceImplementation;
import space.hideaway.services.SecurityServiceImplementation;
import space.hideaway.services.UserManagementImpl;

import java.util.Comparator;
import java.util.Set;


/**
 * The controller responsible for displaying the dashboard, as
 * well as any associated endpoints for this aspect of the application.
 */
@Controller
public class DashboardController {

    /**
     * The service responsible for user management operations.
     */
    private final UserManagementImpl userManagementImpl;

    /**
     * The service responsible for dashboard specific operations.
     */
    private final DashboardServiceImplementation dashboardServiceImplementation;


    /**
     * Obtain the default temperature unit from the application.properties file and inject
     * it into a variable for later user.
     */
    @Value("${cocotemp.temperature.unit}")
    String temperatureUnit;

    @Autowired
    public DashboardController(UserManagementImpl userManagementImpl, DashboardServiceImplementation dashboardServiceImplementation, SecurityServiceImplementation securityServiceImplementation, DataServiceImplementation dataServiceImplementation) {
        this.userManagementImpl = userManagementImpl;
        this.dashboardServiceImplementation = dashboardServiceImplementation;
    }

    /**
     * The endpoint for the dashboard view.
     * @param model The model maintained by Spring.
     * @return The dashboard template name.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        //Obtain the logged in user.
        User user = userManagementImpl.getCurrentLoggedInUser();

        //Inject the list of devices of the user into the model.
        Set<Device> deviceSet = user.getDeviceSet();
        model.addAttribute("devices", deviceSet);
        model.addAttribute("devicesComparator", (Comparator<Device>) (o1, o2) -> o1.getDeviceName().compareTo(o2.getDeviceName()));

        //Not used currently, but will be.
        model.addAttribute("dashboardServiceImplementation", dashboardServiceImplementation);
        model.addAttribute("temperatureUnit", temperatureUnit);

        //Refers to dashboard.html.
        return "dashboard";
    }
}
