package space.hideaway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import space.hideaway.model.Device;

import java.util.ArrayList;


@Component
public class DeviceValidator {

    private final
    Environment environment;

    private boolean hasError = false;

    private ArrayList<String> errors;

    @Autowired
    public DeviceValidator(Environment environment) {
        this.errors = new ArrayList<>();
        this.environment = environment;
    }


    public void validate(Device device) {
        this.hasError = false;
        this.errors = new ArrayList<>();
        if (device.getDeviceName().isEmpty() || device.getDeviceLatitude() == 0) {
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
