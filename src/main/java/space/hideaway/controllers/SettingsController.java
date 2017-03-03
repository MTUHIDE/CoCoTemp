package space.hideaway.controllers;

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
import space.hideaway.model.User;
import space.hideaway.services.DeviceService;
import space.hideaway.services.UserManagementImpl;
import space.hideaway.validation.DeviceQuestionnaireValidator;
import space.hideaway.validation.NewDeviceValidator;
import space.hideaway.validation.PersonalDetailsValidator;

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
    DeviceService deviceService;

    private final
    NewDeviceValidator deviceValidator;
    private final
    DeviceQuestionnaireValidator deviceQuestionnaireValidator;

    private final
    PersonalDetailsValidator personalDetailsValidator;

    @Autowired
    public SettingsController(
            UserManagementImpl userManagement,
            DeviceService deviceService,
            NewDeviceValidator deviceValidator,
            DeviceQuestionnaireValidator deviceQuestionnaireValidator,
            PersonalDetailsValidator personalDetailsValidator)
    {
        this.userManagement = userManagement;
        this.deviceService = deviceService;
        this.deviceValidator = deviceValidator;
        this.deviceQuestionnaireValidator = deviceQuestionnaireValidator;
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
        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "settings/general";
    }

    /**
     * The controller method for the device settings page.
     *
     * @param model    The model maintained by Spring for the device settings page.
     * @param deviceID The ID of the device to show the settings page for.
     * @return The path to the device page template of the settings group.
     */
    @RequestMapping(value = "/settings/device", params = {"deviceID"})
    public String loadDevice(Model model, @RequestParam("deviceID") UUID deviceID)
    {
        User currentLoggedInUser = userManagement.getCurrentLoggedInUser();

        if (!deviceService.isCorrectUser(currentLoggedInUser, deviceID.toString()))
        {
            //TODO create this page for user has no access to device settings.
            return "error/no-access";
        }

        if (!model.containsAttribute("device"))
        {
            model.addAttribute("device", deviceService.findByKey(deviceID.toString()));
        }

        model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
        return "settings/device";
    }

    /**
     * The controller mapping for update a device's properties via a post request from a form.
     *
     * @param model              The model maintained by Spring for the settings/device/update endpoint.
     * @param device             The device that has been edited.
     * @param bindingResult      The module for linking validation errors to the template.
     * @param redirectAttributes Model for persisting objects across a redirect as opposed to a
     *                           new rendering of a template.
     * @return A redirect command to the settings page for the device that has been edited or failed to be
     * edited.
     */
    @RequestMapping(value = "/settings/device/update", method = RequestMethod.POST)
    public String updateDevice(
            Model model,
            @ModelAttribute("device") Device device,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes)
    {
        deviceValidator.validate(device, bindingResult);
        deviceQuestionnaireValidator.validate(device, bindingResult);
        if (bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.device", bindingResult);
            redirectAttributes.addFlashAttribute("device", device);
            return "redirect:/settings/device?deviceID=" + device.getId().toString();
        }
        deviceService.save(device);
        return "redirect:/settings/device?deviceID=" + device.getId().toString();
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
            model.addAttribute("devices", currentLoggedInUser.getDeviceSet());
            return "settings/general";
        }
        userManagement.update(user);
        return "redirect:/settings";
    }
}
