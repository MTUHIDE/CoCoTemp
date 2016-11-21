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
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by dough on 10/12/2016.
 */
@Service
public class DeviceServiceImplementation implements DeviceService {

    private final UserManagementImpl userManagementImpl;
    private final SecurityServiceImplementation securityServiceImplementation;
    private final DeviceRepository deviceRepository;
    private final DeviceValidator deviceValidator;
    private final DataService dataService;
    Logger logger = Logger.getLogger(getClass());

    @Autowired
    public DeviceServiceImplementation(DataService dataService, DeviceValidator deviceValidator, UserManagementImpl userManagementImpl, SecurityServiceImplementation securityServiceImplementation, DeviceRepository deviceRepository) {
        this.dataService = dataService;
        this.deviceValidator = deviceValidator;
        this.userManagementImpl = userManagementImpl;
        this.securityServiceImplementation = securityServiceImplementation;
        this.deviceRepository = deviceRepository;
    }

    public Data getLastRecording(Device device) {
        return dataService.getLastRecording(device);
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
            id = userManagementImpl.findByUsername(loggedInUsername).getId();
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
        return deviceRepository.findOne(UUID.fromString(deviceKey));
    }

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public boolean isCorrectUser(User user, String deviceKey) {
        boolean found = false;
        Set<Device> deviceSet = user.getDeviceSet();
        for (Device device : deviceSet) {
            if (device.getId().toString().equals(deviceKey)) {
                found = true;
            }
        }
        return found;
    }

    public boolean isCorrectUser(String deviceKey) {
        return isCorrectUser(userManagementImpl.getCurrentLoggedInUser(), deviceKey);
    }
}
