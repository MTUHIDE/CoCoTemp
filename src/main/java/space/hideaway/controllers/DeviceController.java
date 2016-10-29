package space.hideaway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import space.hideaway.model.Device;
import space.hideaway.services.DeviceServiceImplementation;

/**
 * HIDE CoCoTemp 2016
 * Class responsible for routing requests relating to CRUD operations on devices.
 *
 * @author Piper Dougherty
 */
@Controller
public class DeviceController {

    @Autowired
    DeviceServiceImplementation deviceServiceImplementation;

    /**
     * Add a new device to a user's device collection. Returns a JSON tree representing the status
     * of the device creation.
     * <p>
     * Example JSON of successful addition.
     * {
     * "error": false
     * }
     * <p>
     * Example JSON of unsuccessful addition.
     * {
     * "error": true,
     * "errors": ["A description of some error one.", "A description of some error two."]
     * }
     *
     * @param deviceName     The name of the new device.
     * @param deviceLocation A string representation of the location of the device.
     * @return A JSON representation of the status.
     */
    @RequestMapping(value = "/manage/devices/add", method = RequestMethod.POST)
    public
    @ResponseBody
    String addDevice(@RequestParam("deviceName") String deviceName,
                     @RequestParam("deviceLocation") String deviceLocation) {
        //Save the new device to the database.
        return deviceServiceImplementation.save(new Device(deviceName, deviceLocation));
    }

}
