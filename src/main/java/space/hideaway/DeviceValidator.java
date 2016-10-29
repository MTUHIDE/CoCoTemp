package space.hideaway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import space.hideaway.model.Device;

import java.util.ArrayList;

/**
 * Created by dough on 10/28/2016.
 */
@Component
public class DeviceValidator {

    @Autowired
    Environment environment;

    private boolean hasError = false;
    private ArrayList<String> errors;

    public DeviceValidator() {
        this.errors = new ArrayList<>();
    }

    public void validate(Device device) {

        //Reset the fields because this class is shared.
        this.hasError = false;
        this.errors = new ArrayList<>();

        //TODO check if device with same name already exists.
        if (device.getDeviceName().isEmpty() || device.getDeviceLocation().isEmpty()) {
            hasError = true;
            errors.add(environment.getProperty("Fields.empty"));
        } else if (device.getDeviceName().length() > 27) {
            hasError = true;
            errors.add(environment.getProperty("Device.longname"));
        }

    }

    public boolean hasErrors() {
        return hasError;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
