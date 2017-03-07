package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service
public class DeviceServiceImplementation implements DeviceService {

    private final UserService userService;
    private final SecurityServiceImplementation securityServiceImplementation;
    private final DeviceRepository deviceRepository;


    @Autowired
    public DeviceServiceImplementation(
            DataService dataService,

            UserService userService,
            SecurityServiceImplementation securityServiceImplementation,
            DeviceRepository deviceRepository)
    {
        this.userService = userService;
        this.securityServiceImplementation = securityServiceImplementation;
        this.deviceRepository = deviceRepository;
    }


    /**
     * Save a new newDevice into the database.
     *
     * @param newDevice The new newDevice to be inserted.
     * @return
     */
    @Override
    public Device save(Device newDevice)
    {
        Long id = userService.getCurrentLoggedInUser().getId();
        newDevice.setUserID(id);
        deviceRepository.save(newDevice);
        return newDevice;
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
