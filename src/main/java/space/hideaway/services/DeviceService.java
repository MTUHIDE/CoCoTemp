package space.hideaway.services;

import space.hideaway.model.Device;

public interface DeviceService {

    /**
     * Save a new device to the database.
     *
     * @param device The new device to be added.
     * @return A JSON representation of the status of the addition.
     */
    String save(Device device);

    Device findByKey(String deviceKey);
}
