package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.hideaway.model.User;
import space.hideaway.services.DeviceService;
import space.hideaway.services.UserManagementImpl;

import java.util.UUID;

/**
 * Created by dough on 1/25/2017.
 */
@Controller
public class SettingsController
{
    @Autowired
    UserManagementImpl userManagement;
    @Autowired
    DeviceService deviceService;

    @RequestMapping(value = "/settings")
    public String showSettings(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "/settings/general";
    }

    @RequestMapping(value = "/settings/device", params = {"deviceID"})
    public String loadDevice(Model model, @RequestParam("deviceID") UUID deviceID)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        model.addAttribute("currentDevice", deviceService.findByKey(deviceID.toString()));
        return "/settings/device";
    }
}
