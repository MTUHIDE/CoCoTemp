package space.hideaway.controllers.newdevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import space.hideaway.model.Device;
import space.hideaway.services.DeviceService;
import space.hideaway.validation.NewDeviceValidator;

/**
 * The controller for the multi-page new device registration. Contains a session attribute "device"
 * to persist a template device across the multiple pages of the new device registration route.
 */
@Controller
@RequestMapping("/settings/new")
@SessionAttributes("device")
public class NewDeviceController
{

    /**
     * The validator for a device.
     */
    private final
    NewDeviceValidator newDeviceValidator;

    /**
     * The service responsible for CRUD operations on devices.
     */
    private final
    DeviceService deviceService;

    @Autowired
    public NewDeviceController(
            NewDeviceValidator newDeviceValidator,
            DeviceService deviceService)
    {
        this.newDeviceValidator = newDeviceValidator;
        this.deviceService = deviceService;
    }

    /**
     * Load the initial page of the new device registration route. Injects a blank
     * device into the model for the user to manipulate over the next few pages.
     * The session begins with this page.
     *
     * @param modelMap The model maintained by Spring for the new device route.
     * @return The path to the new-device-form template.
     */
    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        //Add a blank device to the session's model.
        modelMap.addAttribute("device", new Device());
        return "/new-device/new-device-form";
    }

    /**
     * The second page of the new device registration route.
     *
     * @param device        The device that exists in the model with fields populated by what the user
     *                      entered into the form on the initial page.
     * @param bindingResult The error module for this page, allows bindings between the validation module
     *                      and the actual page.
     * @return The path to the device-questionnaire template, or the path to the new-device-form
     * if validation has failed on fields from the first page.
     */
    @RequestMapping(params = "_questions", method = RequestMethod.POST)
    public String questionPage(
            final @ModelAttribute("device") Device device,
            final BindingResult bindingResult
    )
    {
        newDeviceValidator.validate(device, bindingResult);
        if (bindingResult.hasErrors())
        {
            return "/new-device/new-device-form";
        }

        return "/new-device/device-questionnaire";
    }

    /**
     * The final page of the new device registration route. Validation is performed on fields from the previous page
     * and the device is persisted if no issues are present.
     *
     * @param device        The device that exists in the model with fields populated by what the user
     *                      entered into the form on the second page.
     * @param bindingResult The error module for this page, allows bindings between the validation module
     *                      and the actual page.
     * @param sessionStatus The session module for the new device registration route.
     * @return A redirect command to the dashboard, or the path to the device-questionnaire if validation
     * has failed on fields from the previous page.
     */
    @RequestMapping(params = "_finish", method = RequestMethod.POST)
    public String createDevice(
            @ModelAttribute("device") Device device,
            BindingResult bindingResult,
            SessionStatus sessionStatus
    )
    {
        //Perform validation on the fields, and redirect to the previous page if errors are present.
        newDeviceValidator.validateFinal(device, bindingResult);
        if (bindingResult.hasErrors())
        {
            return "/new-device/device-questionnaire";
        }

        //Persist the device.
        deviceService.save(device);

        //Set the session complete, as the device has been safely persisted.
        sessionStatus.setComplete();

        //Redirect to the dashboard.
        return "redirect:/dashboard";
    }
}
