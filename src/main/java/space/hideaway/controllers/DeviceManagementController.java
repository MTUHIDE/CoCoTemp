package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.model.Device;
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

    /**
     * The API endpoint for creating a device.
     * <p>
     * Secured: Yes
     * URL: /manage/devices/add
     * Method: POST
     * <p>
     * Sample Successful
     * {error: false}
     * <p>
     * Sample Failed
     * {error: true, errors: ["Sample error description."]}
     *
     * @param deviceName      The name of the device.
     * @param deviceLatitude  The latitude of the device.
     * @param deviceLongitude The longitude of the device.
     * @return A json representation of the status.
     */
    @RequestMapping(value = "/manage/devices/add", method = RequestMethod.POST)
    public
    @ResponseBody
    String addDevice(@RequestParam("deviceName") String deviceName,
                     @RequestParam("deviceLatitude") double deviceLatitude,
                     @RequestParam("deviceLongitude") double deviceLongitude) {
        return deviceService.save(new Device(deviceName, deviceLatitude, deviceLongitude));
    }

}
