package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.List;

public interface DeviceService {

    Data getLastRecording(Device device);

    /**
     * Save a new device to the database.
     *
     * @param device The new device to be added.
     * @return A JSON representation of the status of the addition.
     */
    String save(Device device);

    Device findByKey(String deviceKey);

    List<Device> getAllDevices();

    boolean isCorrectUser(User user, String deviceKey);
}
