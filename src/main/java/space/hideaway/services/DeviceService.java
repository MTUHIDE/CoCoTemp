package space.hideaway.services;

import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.List;

public interface DeviceService {

    /**
     * Save a new device into the database.
     *
     * @param device The new device to be inserted.
     * @return
     */
    Device save(Device device);

    /**
     * Find a device by device ID.
     *
     * @param deviceID The ID of the device to obtain.
     * @return The obtained device matching the given deviceID.
     */
    Device findByKey(String deviceID);

    /**
     * Obtain a list of all devices.
     *
     * @return A list of all devices.
     */
    List<Device> getAllDevices();

    /**
     * Compare a deviceID and a user, and determine whether the relationship between
     * them is valid.
     *
     * @param user     The user to compare to the device.
     * @param deviceID The ID of the device to compare to the user.
     * @return True if the user owns the device, false otherwise.
     */
    boolean isCorrectUser(User user, String deviceID);

    /**
     * Compare a deviceID and the currently logged in user, and determine whether the relationship between
     * them is valid.
     *
     * @return True if the user owns the device, false otherwise.
     */
    boolean isCorrectUser(String deviceKey);

    Long countByUserID(User currentLoggedInUser);
}
