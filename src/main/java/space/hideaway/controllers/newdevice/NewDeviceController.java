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
import space.hideaway.validation.NewDeviceValidator;

/**
 * Created by dough on 2017-02-09.
 */
@Controller
@RequestMapping("/settings/new")
@SessionAttributes("device")
public class NewDeviceController
{

    @Autowired
    NewDeviceValidator newDeviceValidator;

    @RequestMapping
    public String initialPage(final ModelMap modelMap)
    {
        modelMap.addAttribute("device", new Device());
        return "/new-device/new-device-form";
    }

    @RequestMapping(params = "_question", method = RequestMethod.POST)
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

    @RequestMapping(params = "_finish", method = RequestMethod.POST)
    public String createDevice(
            @ModelAttribute("device") Device device,
            BindingResult bindingResult,
            ModelMap modelMap,
            SessionStatus sessionStatus
    )
    {
        sessionStatus.setComplete();
        return "/dashboard";
    }
}
