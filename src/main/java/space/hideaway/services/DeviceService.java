package space.hideaway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.hideaway.model.Device;
import space.hideaway.repositories.DeviceRepository;

import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device save(Device device) {
        deviceRepository.save(device);
        return device;
    }

    public Device findByKey(String deviceID) {
        return deviceRepository.getOne(UUID.fromString(deviceID));
    }
}
