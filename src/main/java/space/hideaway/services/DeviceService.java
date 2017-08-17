package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.model.User;
import space.hideaway.repositories.DeviceRepository;

import java.util.Set;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @Autowired
    public DeviceService(
            DeviceRepository deviceRepository,
            UserService userService)
    {
        this.userService = userService;
        this.deviceRepository = deviceRepository;
    }

    public Device save(Device device)
    {
        Long id = userService.getCurrentLoggedInUser().getId();
        device.setUserID(id);
        deviceRepository.save(device);
        return device;
    }

    public void delete(Device device)
    {
        deviceRepository.deleteById(device.getId());
    }

    public Device findByKey(String deviceID)
    {
        return deviceRepository.getOne(UUID.fromString(deviceID));
    }

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

    public Long countByUserID(User currentLoggedInUser)
    {
        return deviceRepository.countByUserID(currentLoggedInUser.getId());
    }
}
