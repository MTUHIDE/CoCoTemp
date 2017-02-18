package space.hideaway.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.Device;

/**
 * Created by dough on 2017-02-09.
 */
@Component
public class NewDeviceValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return Device.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        Device device = (Device) target;
        if (device.getDeviceName().length() > 27)
        {
            errors.rejectValue("deviceName", "Device.longName");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deviceName", "Device.emptyName");
    }

    public void validateFinal(Device device, BindingResult bindingResult)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "deviceDescription", "Device.emptyDescription");
    }
}
