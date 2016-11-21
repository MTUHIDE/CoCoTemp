package space.hideaway.services;

import space.hideaway.model.Data;
import space.hideaway.model.Device;
import space.hideaway.model.User;

import java.util.List;

public interface DeviceService {

    Data getLastRecording(Device device);


    String save(Device device);

    Device findByKey(String deviceKey);

    List<Device> getAllDevices();

    boolean isCorrectUser(User user, String deviceKey);
}
