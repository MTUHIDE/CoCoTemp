package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;
import space.hideaway.services.user.UserService;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Justin Havely
 * 6/7/17
 */
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @Autowired
    private Environment env;


    @Autowired
    public DeviceService(
            DeviceRepository deviceRepository,
            UserService userService)
    {
        this.userService = userService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Saves a device into the database. Automatically sets the user who created the device.
     *
     * @param device The device to save
     * @return The saved device
     */
    public Device save(Device device)
    {
        Long id = userService.getCurrentLoggedInUser().getId();
        device.setUserID(id);
        deviceRepository.save(device);
        return device;
    }


    public void update(Device device)
    {
        Device oldDevice = deviceRepository.getOne(device.getId());

        if (oldDevice != null)
        {
            if (device.getDeviceType() == null)
                device.setDeviceType(oldDevice.getDeviceType());
            if (device.getDataSet() == null)
                device.setDataSet(oldDevice.getDataSet());
            if (device.getManufacture_num() == null)
                device.setManufacture_num(oldDevice.getManufacture_num());
            if (device.getId() == null)
                device.setId(oldDevice.getId());
            if (device.getUploadHistories() == null)
                device.setUploadHistories(oldDevice.getUploadHistories());
            if (device.getSiteID() == null)
                device.setSiteID(oldDevice.getSiteID());
            if (device.getShelterType() == null)
                device.setShelterType(oldDevice.getShelterType());
            if (device.getSite() == null)
                device.setSite(oldDevice.getSite());
        }

        deviceRepository.save(device);
    }
    /**
     * Deletes a device from the database.
     *
     * @param device The device to be deleted
     */
    public void delete(Device device)
    {
        device.setUserID((long)Integer.parseInt(env.getProperty("spring.admin.id")));
        device.setUser(null);
        this.update(device);
    }

    /**
     * Finds a device by its id.
     *
     * @param deviceID the device's id
     * @return the device with the given id
     */
    public Device findByKey(String deviceID)
    {
        return deviceRepository.getOne(UUID.fromString(deviceID));
    }

    /**
     * Checks if the user owns the device.
     *
     * @param user The user
     * @param deviceID The device
     * @return True if the user owns the device
     */
    public boolean isCorrectUser(User user, String deviceID)
    {
        if (user == null) return false;

        boolean found = false;
        Set<Device> deviceSet = user.getDeviceSet();

        for (Device device : deviceSet)
        {
            if (device.getId().toString().equals(deviceID)) found = true;
        }
        return found;
    }

    /**
     * Gets the count of devices created by an user.
     *
     * @param currentLoggedInUser The user id of the currently login user
     * @return The number of devices.
     */
    public Long countByUserID(User currentLoggedInUser)
    {
        return deviceRepository.countByUserID(currentLoggedInUser.getId());
    }
}
