package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import space.hideaway.services.DeviceService;
import space.hideaway.services.DeviceServiceImplementation;

/**
 * The controller responsible for endpoints relating to the
 * management of devices. Specifically, CRUD operations on devices.
 */
@Controller
public class DeviceManagementController {

    /**
     * The service responsible for operations on devices. Specifically, CRUD operations
     * on devices.
     */
    private final DeviceService deviceService;

    @Autowired
    public DeviceManagementController(DeviceServiceImplementation deviceService) {
        this.deviceService = deviceService;
    }


    @RequestMapping(value = "/manage/devices/add")
    public String addDevice()
    {
        return "deviceCreationLanding";
    }

}
