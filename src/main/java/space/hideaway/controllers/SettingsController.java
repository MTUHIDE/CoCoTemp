package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import space.hideaway.model.Device;
import space.hideaway.model.Site;
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;
import space.hideaway.services.SiteService;
import space.hideaway.services.UserManagementImpl;
import space.hideaway.validation.NewSiteValidator;
import space.hideaway.validation.PersonalDetailsValidator;
import space.hideaway.validation.SiteQuestionnaireValidator;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by dough on 1/25/2017.
 */
@Controller
public class SettingsController
{
    private final
    UserManagementImpl userManagement;
    private final
    SiteService siteService;
    private final
    NewSiteValidator siteValidator;
    private final
    SiteQuestionnaireValidator siteQuestionnaireValidator;
    private final DeviceRepository deviceRepository;
    private final
    PersonalDetailsValidator personalDetailsValidator;

    @Autowired
    public SettingsController(
            UserManagementImpl userManagement,
            SiteService siteService,
            NewSiteValidator siteValidator,
            SiteQuestionnaireValidator siteQuestionnaireValidator,
            PersonalDetailsValidator personalDetailsValidator,
            DeviceRepository deviceRepository)
    {
        this.userManagement = userManagement;
        this.siteService = siteService;
        this.siteValidator = siteValidator;
        this.deviceRepository = deviceRepository;
        this.siteQuestionnaireValidator = siteQuestionnaireValidator;
        this.personalDetailsValidator = personalDetailsValidator;
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
     * The controller method for the site settings page.
     *
     * @param model    The model maintained by Spring for the site settings page.
     * @param siteID The ID of the site to show the settings page for.
     * @return The path to the site page template of the settings group.
     */
    @RequestMapping(value = "/settings/site", params = {"siteID"})
    public String loadSite(Model model, @RequestParam("siteID") UUID siteID)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();

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
        return "settings/site";
    }

    /**
     * The controller mapping for update a site's properties via a post request from a form.
     *
     * @param model              The model maintained by Spring for the settings/site/update endpoint.
     * @param site             The site that has been edited.
     * @param bindingResult      The module for linking validation errors to the template.
     * @param redirectAttributes Model for persisting objects across a redirect as opposed to a
     *                           new rendering of a template.
     * @return A redirect command to the settings page for the site that has been edited or failed to be
     * edited.
     */
    @RequestMapping(value = "/settings/site/update", method = RequestMethod.POST)
    public String updateSite(
            Model model,
            @ModelAttribute("site") Site site,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        siteValidator.validate(site, bindingResult);
        siteQuestionnaireValidator.validate(site, bindingResult);
        if (bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.site", bindingResult);
            redirectAttributes.addFlashAttribute("site", site);
            return "redirect:/settings/site?siteID=" + site.getId().toString();
        }
        siteService.save(site);
        return "redirect:/settings/site?siteID=" + site.getId().toString();
    }

    @RequestMapping(value = "/settings/update", method = RequestMethod.POST)
    public String updateGeneral(
            Model model,
            @ModelAttribute("user") User user,
            BindingResult bindingResult
    )
    {
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

    @RequestMapping(value = "/settings/new/device", method = RequestMethod.GET)
    public String newDevice(Model model){
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        model.addAttribute("sites", currentLoggedInUser.getSiteSet());
        return "/newDevice";
    }

    @RequestMapping(value = "/settings/new/device", method = RequestMethod.POST)
    public String addDevice(@RequestParam (value = "siteID") UUID siteID,
                            @RequestParam (value = "deviceType") String deviceType,
                            @RequestParam (value = "Manufacture_num") String Manufacture_num){
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();
        Device device = new Device();
        device.setSiteID(siteID);
        device.setUserID(currentLoggedInUser.getId());
        device.setType(deviceType);
        device.setManufacture_num(Manufacture_num);
        deviceRepository.save(device);
        return "redirect:/settings";
    }

    

}
