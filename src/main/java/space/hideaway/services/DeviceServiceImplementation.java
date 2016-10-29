package space.hideaway.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import space.hideaway.DeviceErrorSerializer;
import space.hideaway.DeviceValidator;
import space.hideaway.model.Device;
import space.hideaway.repositories.DeviceRepository;

/**
 * Created by dough on 10/12/2016.
 */
@Service
public class DeviceServiceImplementation implements DeviceService {

    @Autowired
    UserServiceImplementation userServiceImplementation;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceValidator deviceValidator;

    @Override
    public String save(Device device) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = userServiceImplementation.findByUsername(authentication.getName()).getId();
        device.setUserId(id);

        deviceValidator.validate(device);

        Gson gson = new GsonBuilder().registerTypeAdapter(DeviceValidator.class, new DeviceErrorSerializer()).create();
        if (deviceValidator.hasErrors()) {
            return gson.toJson(deviceValidator);
        } else {
            deviceRepository.save(device);
        }
        return gson.toJson(deviceValidator);
    }
}
