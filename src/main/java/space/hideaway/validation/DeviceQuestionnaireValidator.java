package space.hideaway.validation;

/**
 * Created by dough on 2017-02-17.
 */

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.Device;

/**
 * Created by dough on 2017-02-09.
 */
@Component
public class DeviceQuestionnaireValidator implements Validator
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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deviceDescription", "Device.emptyDescription");
    }
}

