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
 * Created by Justin on 6/27/2017.
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

    @GetMapping(value = "/device/new")
    public String newDevice(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("device", new Device());
        return "/newDevice";
    }

    @RequestMapping(value = "/device/new", method = RequestMethod.POST)
    public String addDevice(@ModelAttribute("device") Device device)
    {
        deviceService.save(device);
        return "redirect:/dashboard";
    }

}
