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
    public DeviceService(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    public Device save(UUID siteId, Long userID, String ManufactureNum, String deviceType) {
        Device device = new Device();
        device.setSiteID(siteId);
        device.setUserID(userID);
        device.setManufacture_num(ManufactureNum);
        device.setType(deviceType);
        deviceRepository.save(device);
        return device;
    }

}
