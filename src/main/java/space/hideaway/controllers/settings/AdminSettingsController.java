package space.hideaway.controllers.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.site.Site;
import space.hideaway.services.DeviceService;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.SiteValidator;
import org.springframework.stereotype.Controller;


import java.util.UUID;

@Controller
public class AdminSettingsController {
    private final UserServiceImplementation userManagement;


    private final PersonalDetailsValidator personalDetailsValidator;


    @Autowired
    public AdminSettingsController(
            UserServiceImplementation userManagement,
            PersonalDetailsValidator personalDetailsValidator
            )
    {
        this.userManagement = userManagement;
        this.personalDetailsValidator = personalDetailsValidator;
    }


    /**
     * The controller method for the settings page.
     *
     * @param model The model maintained by Spring for the settings page.
     * @return The path to the general page template of the settings group.
     */
    @RequestMapping(value = "/adminsettings")
    public String showSettings(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("user", currentLoggedInUser);
        return "adminsettings/admin-general";
    }

    /**
     * The controller mapping to update a user's properties via a post request from.
     *
     * @param model The model maintained by Spring for the site settings page.
     * @param user The user that has been edited.
     * @param bindingResult The module for linking validation errors to the template.
     * @return Redirect command to the general settings page
     */
    @RequestMapping(value = "/adminsettings/update", method = RequestMethod.POST)
    public String updateGeneral(
            Model model,
            @ModelAttribute("user") User user,
            BindingResult bindingResult)
    {
        // Validates first, middle, and last name fields.
        personalDetailsValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
        {
            User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
            return "adminsettings/admin-general";
        }

        userManagement.update(user);
        return "redirect:/adminsettings";
    }
}
