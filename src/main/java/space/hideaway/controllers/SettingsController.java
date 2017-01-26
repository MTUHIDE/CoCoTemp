package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.model.User;
import space.hideaway.services.UserManagementImpl;

/**
 * Created by dough on 1/25/2017.
 */
@Controller
public class SettingsController
{
    @Autowired
    UserManagementImpl userManagement;

    @RequestMapping(value = "/settings")
    public String showSettings(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "/settings/general";
    }

    @RequestMapping(value = "/settings/profile")
    public String defaultProfile(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "/settings/profile";
    }
}
