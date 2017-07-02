package space.hideaway.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import space.hideaway.model.Device;


/**
 * Created by Justin on 6/29/2017.
 */
@Component
public class NewDeviceValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Device.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Device device = (Device) target;

        if(device.getType().equals("Other")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "otherType", "Device.emptyType");
        }
    }
}
