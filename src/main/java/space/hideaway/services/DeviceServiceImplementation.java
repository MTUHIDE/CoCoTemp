package space.hideaway.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.DeviceErrorSerializer;
import space.hideaway.DeviceValidator;
import space.hideaway.UserNotFoundException;
import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.repositories.DeviceRepository;

import java.util.List;
import java.util.UUID;

/**
 * Created by dough on 10/12/2016.
 */
@Service
public class DeviceServiceImplementation implements DeviceService {

    private final UserServiceImplementation userServiceImplementation;
    private final SecurityServiceImplementation securityServiceImplementation;
    private final DeviceRepository deviceRepository;
    private final DeviceValidator deviceValidator;
    Logger logger = Logger.getLogger(getClass());

    @Autowired
    public DeviceServiceImplementation(DeviceValidator deviceValidator, UserServiceImplementation userServiceImplementation, SecurityServiceImplementation securityServiceImplementation, DeviceRepository deviceRepository) {
        this.deviceValidator = deviceValidator;
        this.userServiceImplementation = userServiceImplementation;
        this.securityServiceImplementation = securityServiceImplementation;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Save a new device to the database. Returns a JSON structure representing the status of the addition.
     * <p>
     * Example JSON of successful addition.
     * {
     * "error": false
     * }
     * <p>
     * Example JSON of unsuccessful addition.
     * {
     * "error": true,
     * "errors" ["A description of some error one.", "A description of some error two."]
     * }
     *
     * @param device The new device to be added.
     * @return A JSON representation of the status of the addition.
     */
    @Override
    public String save(Device device) {
        //Obtain the security context of the currently logged in user.
        String loggedInUsername = securityServiceImplementation.findLoggedInUsername();
        Long id = null;
        try {
            id = userServiceImplementation.findByUsername(loggedInUsername).getId();
        } catch (UserNotFoundException e) {
            logger.error("The user was not found when attempting to create a new device", e);
        }

        //We must associate the device with a user.
        device.setUserId(id);

        //Is the device of valid format?
        deviceValidator.validate(device);

        //Format JSON.
        Gson gson = new GsonBuilder().registerTypeAdapter(DeviceValidator.class, new DeviceErrorSerializer()).create();
        if (deviceValidator.hasErrors()) {
            return gson.toJson(deviceValidator);
        } else {
            deviceRepository.save(device);
        }
        return gson.toJson(deviceValidator);
    }

    @Override
    public Device findByKey(String deviceKey) {
        UUID formattedUUID = UUID.fromString(deviceKey);
        return deviceRepository.findByDeviceKey(formattedUUID);
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
