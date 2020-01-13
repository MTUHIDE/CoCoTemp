package space.hideaway.controllers.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.model.Device;
import space.hideaway.model.site.Site;
import space.hideaway.model.User;
import space.hideaway.services.DeviceService;
import space.hideaway.services.site.SiteService;
import space.hideaway.services.user.UserServiceImplementation;
import space.hideaway.validation.SiteValidator;
import space.hideaway.validation.PersonalDetailsValidator;

import java.util.UUID;

/**
 * Created by dough
 * 1/25/2017
 *
 * Edited by Justin Havely
 * 8/18/17
 *
 * Serves the settings pages to the user.
 */
@Controller
public class SettingsController
{
    private final UserServiceImplementation userManagement;

    private final SiteService siteService;
    private final DeviceService deviceService;

    private final SiteValidator siteValidator;
    private final PersonalDetailsValidator personalDetailsValidator;


    @Autowired
    public SettingsController(
            UserServiceImplementation userManagement,
            SiteService siteService,
            SiteValidator siteValidator,
            PersonalDetailsValidator personalDetailsValidator,
            DeviceService deviceService)
    {
        this.userManagement = userManagement;
        this.siteService = siteService;
        this.siteValidator = siteValidator;
        this.personalDetailsValidator = personalDetailsValidator;
        this.deviceService = deviceService;
    }


    /**
     * The controller method for the settings page.
     *
     * @param model The model maintained by Spring for the settings page.
     * @return The path to the general page template of the settings group.
     */
    @RequestMapping(value = "/settings")
    public String showSettings(Model model)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("user", currentLoggedInUser);
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "settings/general";
    }

    /**
     * The controller mapping to update a user's properties via a post request from.
     *
     * @param model The model maintained by Spring for the site settings page.
     * @param user The user that has been edited.
     * @param bindingResult The module for linking validation errors to the template.
     * @return Redirect command to the general settings page
     */
    @RequestMapping(value = "/settings/update", method = RequestMethod.POST)
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
            model.addAttribute("sites", currentLoggedInUser.getSiteSet());
            model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
            return "settings/general";
        }

        userManagement.update(user);
        return "redirect:/settings";
    }

    /**
     * The controller method for the site settings page.
     *
     * @param model The model maintained by Spring for the site settings page.
     * @param siteID The ID of the site to show the settings page for.
     * @return The path to the site page template of the settings group.
     */
    @RequestMapping(value = "/settings/site", params = {"siteID"})
    public String loadSite(Model model, @RequestParam("siteID") UUID siteID)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();

        // Checks if the user has ownership of the site
        if (!siteService.isCorrectUser(currentLoggedInUser, siteID.toString()))
        {
            //TODO create this page for user has no access to site settings.
            return "error/no-access";
        }

        if (!model.containsAttribute("site"))
        {
            model.addAttribute("site", siteService.findByKey(siteID.toString()));
        }
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        model.addAttribute("siteDevices", siteService.findByKey(siteID.toString()).getDeviceSet());
        return "settings/site";
    }

    /**
     * The controller mapping for update a site's properties via a post request from.
     *
     * @param site The site that has been edited.
     * @param bindingResult The module for linking validation errors to the template.
     * @param redirectAttributes Model for persisting objects across a redirect as opposed to a
     *                           new rendering of a template.
     * @return A redirect command to the settings page for the site that has been edited or failed to be
     * edited.
     */
    @RequestMapping(value = "/settings/site/update", method = RequestMethod.POST)
    public String updateSite(
            @ModelAttribute("site") Site site,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        // Validates siteName and siteDescription
        siteValidator.validate(site, bindingResult);
        siteValidator.validateDescription(site, bindingResult);

        if (bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.site", bindingResult);
            redirectAttributes.addFlashAttribute("site", site);
            return "redirect:/settings/site?siteID=" + site.getId().toString();
        }

        siteService.save(site);

        return "redirect:/settings/site?siteID=" + site.getId().toString();
    }

    /**
     *
     * The controller method for the device settings page.
     *
     * @param model The model maintained by Spring for the site settings page.
     * @param deviceID The ID of the device to show the settings page for.
     * @return The path to the site page template of the settings group.
     */
    @RequestMapping(value = "/settings/device", params = {"deviceID"})
    public String loadDevice(
            Model model,
            @RequestParam("deviceID") UUID deviceID)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();

        // Checks if the user has ownership of the site.
        if (!deviceService.isCorrectUser(currentLoggedInUser, deviceID.toString()))
        {
            //TODO create this page for user has no access to site settings.
            return "error/no-access";
        }

        if (!model.containsAttribute("device"))
        {
            model.addAttribute("device", deviceService.findByKey(deviceID.toString()));
        }
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "settings/device";
    }

    /**
     * The controller method for editing a device.
     *
     * @param device The device that has been edited.
     * @return A redirect command to the settings page for the device that has been edited or failed to be
     * edited.
     */
    @RequestMapping(value = "/settings/device", params = {"update"}, method = RequestMethod.POST)
    public String updateDevice(@ModelAttribute("device") Device device)
    {
        deviceService.save(device);
        return "redirect:/settings/device?deviceID=" + device.getId().toString();
    }

    /**
     * The controller method for deleting a device.
     *
     * @param device The device that has been delete.
     * @return Redirect command to the general settings page
     */
    @RequestMapping(value = "/settings/device", params = {"delete"}, method = RequestMethod.POST)
    public String deleteDevice(@ModelAttribute("device") Device device)
    {
        deviceService.delete(device);
        return "redirect:/settings/";
    }

}
