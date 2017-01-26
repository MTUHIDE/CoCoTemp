package space.hideaway.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.model.json.DeviceErrorSerializer;
import space.hideaway.repositories.DeviceRepository;
import space.hideaway.validation.DeviceValidator;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class DeviceServiceImplementation implements DeviceService {

    private final UserService userService;
    private final SecurityServiceImplementation securityServiceImplementation;
    private final DeviceRepository deviceRepository;
    private final DeviceValidator deviceValidator;
    Logger logger = Logger.getLogger(getClass());

    @Autowired
    public DeviceServiceImplementation(
            DataService dataService,
            DeviceValidator deviceValidator,
            UserService userService,
            SecurityServiceImplementation securityServiceImplementation,
            DeviceRepository deviceRepository)
    {
        this.deviceValidator = deviceValidator;
        this.userService = userService;
        this.securityServiceImplementation = securityServiceImplementation;
        this.deviceRepository = deviceRepository;
    }


    /**
     * Save a new device into the database.
     *
     * @param device The new device to be inserted.
     * @return
     */
    @Override
    public String save(Device device) {
        Long id = userService.getCurrentLoggedInUser().getId();
        device.setUserID(id);
        deviceValidator.validate(device);
        Gson gson = new GsonBuilder().registerTypeAdapter(DeviceValidator.class, new DeviceErrorSerializer()).create();
        if (deviceValidator.hasErrors()) {
            return gson.toJson(deviceValidator);
        } else {
            deviceRepository.save(device);
        }
        return gson.toJson(deviceValidator);
    }

    /**
     * Find a device by device ID.
     *
     * @param deviceID The ID of the device to obtain.
     * @return The obtained device matching the given deviceID.
     */
    @Override
    public Device findByKey(String deviceID) {
        return deviceRepository.findOne(UUID.fromString(deviceID));
    }

    /**
     * Obtain a list of all devices.
     *
     * @return A list of all devices.
     */
    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    /**
     * Compare a deviceID and a user, and determine whether the relationship between
     * them is valid.
     *
     * @param user     The user to compare to the device.
     * @param deviceID The ID of the device to compare to the user.
     * @return True if the user owns the device, false otherwise.
     */
    @Override
    public boolean isCorrectUser(User user, String deviceID) {
        if (user == null) {
            return false;
        }
        boolean found = false;
        Set<Device> deviceSet = user.getDeviceSet();
        for (Device device : deviceSet) {
            if (device.getId().toString().equals(deviceID)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Compare a deviceID and the currently logged in user, and determine whether the relationship between
     * them is valid.
     *
     * @return True if the user owns the device, false otherwise.
     */
    public boolean isCorrectUser(String deviceKey) {
        return isCorrectUser(userService.getCurrentLoggedInUser(), deviceKey);
    }

    @Override
    public Long countByUserID(User currentLoggedInUser)
    {
        return deviceRepository.countByUserID(currentLoggedInUser.getId());
    }
}
