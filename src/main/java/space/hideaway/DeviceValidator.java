package space.hideaway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import space.hideaway.model.Device;

import java.util.ArrayList;

/**
 * HIDE CoCoTemp 2016
 * Class reponsible for validation on new devices.
 *
 * @author Piper Dougherty
 */
@Component
public class DeviceValidator {

    /**
     * The Spring environment.
     */
    private final
    Environment environment;

    /**
     * Boolean flag representing whether or not the device has errors.
     */
    private boolean hasError = false;

    /**
     * A list of errors the device contains. Can be empty if the device has no errors.
     */
    private ArrayList<String> errors;

    @Autowired
    public DeviceValidator(Environment environment) {
        this.errors = new ArrayList<>();
        this.environment = environment;
    }

    /**
     * Validate a devices.
     * <p>
     * Checks for the following properties:
     * - Whether the device name or location fields are empty.
     * - Whether the device name is longer than 27 characters.
     *
     * @param device The device to be validated.
     */
    public void validate(Device device) {

        //Reset the fields because this class is shared.
        this.hasError = false;
        this.errors = new ArrayList<>();

        //TODO check if device with same name already exists.
        if (device.getDeviceName().isEmpty() || device.getDeviceLatitude() == 0) {
            hasError = true;
            errors.add(environment.getProperty("Fields.empty"));
        } else if (device.getDeviceName().length() > 27) {
            hasError = true;
            errors.add(environment.getProperty("Device.longname"));
        }

    }

    /**
     * Check if the device has errors.
     *
     * @return True if the device contains an error, or false otherwise.
     */
    public boolean hasErrors() {
        return hasError;
    }

    /**
     * Get the list of errors this device contains.
     *
     * @return A list of errors, or empty if the device has no errors.
     */
    public ArrayList<String> getErrors() {
        return errors;
    }
}
