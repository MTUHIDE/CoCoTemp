package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.services.DeviceService;
import space.hideaway.services.UserServiceImplementation;


/**
 * Created by Justin Havely
 * 6/27/2017
 *
 * Serves the device registration page to the user.
 */
@Controller
public class NewDeviceController
{

    private final UserServiceImplementation userManagement;
    private final DeviceService deviceService;

    @Autowired
    public NewDeviceController(
            UserServiceImplementation userManagement,
            DeviceService deviceService)
    {
        this.deviceService = deviceService;
        this.userManagement = userManagement;
    }

    /**
     * The endpoint for the new device page. This page
     * contains the site, type, and Manufacture number fields.
     * Secured: NO
     * Method: GET
     *
     * Sample URL: /device/new
     * @param model The model maintained by Spring for a new device.
     * @return The path to the new device template.
     */
    @GetMapping(value = "/device/new")
    public String newDevice(Model model)
    {
        // Gets the user's sites and add a new device object to the model.
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("device", new Device());
        return "/newDevice";
    }

    /**
     * The endpoint for the new device page.
     * Secured: NO
     * Method: POST
     *
     * Sample URL: /device/new
     * @param device The device that exists in the model with fields populated by what the user
     *               entered into the form.
     * @return A redirect command to the dashboard (aka "My Sites") template.
     */
    @RequestMapping(value = "/device/new", method = RequestMethod.POST)
    public String addDevice(@ModelAttribute("device") Device device)
    {
        deviceService.save(device);
        return "redirect:/dashboard";
    }

}
